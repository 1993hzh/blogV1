package edu.cuit.hzhspace.controller;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.cuit.hzhspace.service.CommentService;
import edu.cuit.hzhspace.service.DiaryService;
import edu.cuit.hzhspace.service.MessageService;
import edu.cuit.hzhspace.service.MoodService;

@Controller
@Scope("prototype")
public class HomeController extends CustomMultiActionController {

	@Resource
	private DiaryService diaryService;
	@Resource
	private MoodService moodService;
	@Resource
	private MessageService messageService;
	@Resource
	private CommentService commentService;

	private static ModelAndView mv = new ModelAndView("home/home");
	
	private static Calendar cal = Calendar.getInstance();
	private static int year = cal.get(Calendar.YEAR);
	private static int month = cal.get(Calendar.MONTH )+1;

	@RequestMapping(value = "/*")
	public ModelAndView index() {
//		firstDiary
//		firstMood
//		firstMessage
//		
//		mostViewDiary
//		mostUpperMood
//		mostCommentDiary
//		mostCommentMood
//		mostCommentMessage
//		
//		lastDiary
//		lastMood
//		lastMessage
//		mv.addObject("home", diaryService.query("select d from Diary d order by date"));
		return mv;
	}

	@RequestMapping(value = "/home/home!index.action")
	public ModelAndView home() {
//		mv.addObject("home", diaryService.query("select d from Diary d order by date"));
		return mv;
	}
}
