package com.ihelin.car.controller;

import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ihelin.car.config.CommonConfig;
import com.ihelin.car.db.entity.User;
import com.ihelin.car.utils.JSON;
import com.ihelin.car.utils.WechatUtil;
import com.ihelin.car.wechat.model.WeixinUserInfo;

@Controller
public class OAuthPageController extends BaseController {

	private static final Log LOGGER = LogFactory.getLog(OAuthPageController.class);

	@RequestMapping(value = "oauth_url")
	public String oauthRedirectUrl(String url, String scope) throws Exception {
		String callbackUrl = CommonConfig.getDomainUrl() + "/oauth_url_callback?" + "url="
				+ URLEncoder.encode(url, "UTF-8");
		if (StringUtils.isEmpty(scope))
			scope = "snsapi_userinfo"; // or: snsapi_userinfo
		String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + CommonConfig.getAppID()
				+ "&redirect_uri=" + URLEncoder.encode(callbackUrl, "UTF-8") + "&response_type=code&scope=" + scope
				+ "&state=#wechat_redirect";
		// LOGGER.info("Oauth redirect url: " + wxUrl);
		return "redirect:" + wxUrl;
	}

	@RequestMapping(value = "oauth_url_callback")
	public String oauthUrlCallback(String url, String code, String state) {
		LOGGER.info("code: " + code + ", state: " + state);
		OauthModel oauthRes = null;
		int retryCount = 0;
		String api = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + CommonConfig.getAppID() + "&secret="
				+ CommonConfig.getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
		while (retryCount < 3) {
			String res = WechatUtil.doGetStr(api);
			if (StringUtils.isEmpty(res)) {
				retryCount++;
			} else {
				oauthRes = JSON.parseObject(res, OauthModel.class);
				if (oauthRes != null)
					break;
			}
		}
		if (oauthRes != null && !StringUtils.isEmpty(url)) {
			if (url.endsWith("&") || url.endsWith("?")) {
				// 直接加openid
			} else if (url.contains("?")) {
				url += "&";
			} else {
				url += "?";
			}
			url += "openId=" + oauthRes.openid + "&accessToken=" + oauthRes.access_token + "&code=" + code + "&state="
					+ state;
			return "redirect:" + url;
		}
		return "redirect:404";
	}

	@RequestMapping(value = "weixin_login.do")
	public String doWeixinLogin(String openId, String from, String accessToken, String code, String state,
			HttpSession session) {
		LOGGER.info("请求微信登录，openid: " + openId);
		if (StringUtils.isNotBlank(openId)) {
			User user = userManager.selectUserByOpenId(openId);
			if (user == null) {
				user = new User();
				String api = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
						+ accessTokenManager.getAccessToken().getToken() + "&openid=" + openId + "&lang=zh_CN";
				String res = WechatUtil.doGetStr(api);
				WeixinUserInfo wxUser = JSON.parseObject(res, WeixinUserInfo.class);
				user.setCity(wxUser.getCity());
				user.setCountry(wxUser.getCountry());
				user.setGender(wxUser.getSex());
				user.setGroupid(wxUser.getGroupid());
				user.setHeadimgurl(wxUser.getHeadimgurl());
				user.setLanguage(wxUser.getLanguage());
				user.setNickName(wxUser.getNickname());
				user.setOpenId(openId);
				user.setStatus(0);
				user.setProvince(wxUser.getProvince());
				user.setRemark(wxUser.getRemark());
				user.setSubscribe(wxUser.getSubscribe());
				user.setSubscribeTime(new Date(wxUser.getSubscribe_time() * 1000));
				user.setTagidList(JSON.toJson(wxUser.getTagid_list()));
				userManager.insertUser(user);
			}
			setWeixinUser(user);
		} else {
			LOGGER.error("openid is blank");
			return "redirect:index";
		}
		if (StringUtils.isNotBlank(from))
			return "redirect:" + from;
		return "redirect:index";
	}

	static class OauthModel {
		public String access_token;
		public String openid;
		public String scope;
	}

}
