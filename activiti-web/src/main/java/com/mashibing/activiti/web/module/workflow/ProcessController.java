package com.mashibing.activiti.web.module.workflow;

import com.mashibing.activiti.api.ActivitiApi;
import com.mashibing.activiti.api.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunzhiqiang
 * @create 2019-01-08-18:42
 */
@RestController
@RequestMapping("/workFlow")
public class ProcessController {
	@Resource
	private ActivitiApi activitiApi;

	@RequestMapping("/getHistoryProcessInstance")
	public Object getHistoryProcessInstance() {
		R historyProcessInstance = activitiApi.getHistoryProcessInstance();
		return historyProcessInstance;
	}


	private void writeImg(HttpServletResponse response, InputStream sbs) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		//生成图片验证码
		BufferedImage image = ImageIO.read(sbs);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "png", out);
	}





}
