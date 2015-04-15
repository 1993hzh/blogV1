package edu.cuit.hzhspace.service;

import java.util.Date;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.AbstractEntity;
import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.Diary;
import edu.cuit.hzhspace.model.Message;
import edu.cuit.hzhspace.model.Mood;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.util.IPSeekerFactory;

@Service
public class CommentService extends AbstractService<Comment> {

	private static Logger logger = Logger.getLogger(CommentService.class);
	
	public CommentService(Class<Comment> entity) {
		super(entity);
	}

	public CommentService() {
		this(Comment.class);
	}

	/**
	 * 增加评论
	 * @param type 类型
	 * @param content 评论内容
	 * @param entity 评论对应实体
	 * @return
	 */
	public void createComment(String type, String content, AbstractEntity entity, User user, String ip)
			throws Exception {
		try {
			Comment comment = new Comment();
			if (user == null) {//非注册用户评论
				comment.setReplyer(getReplierInfo(ip));
			} else {
				comment.setReplyer(user.getName());
			}
			comment.setIp(ip);
			comment.setContent(content);
			comment.setDate(new Date());
			if (type.equals(Comment.COMMENT_TYPE_DIARY)) {
				comment.setDiary((Diary) entity);
			}
			if (type.equals(Comment.COMMENT_TYPE_MESSAGE)) {
				comment.setMessage((Message) entity);
			}
			if (type.equals(Comment.COMMENT_TYPE_MOOD)) {
				comment.setMood((Mood) entity);
			}
			comment.setType(type);
			comment.setPrincipalId(entity.getId());
			emWrite.persist(comment);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 删除评论
	 * 具体的权限判断由controller控制
	 * @param id
	 * @return
	 */
	public void removeComment(String id) throws Exception {
		try {
			//先删除评论下的回复
			emWrite.createNativeQuery("delete from T_CommentReply where comment_id = ?").setParameter(1, id)
					.executeUpdate();
			//再删除评论主体
			emWrite.createNativeQuery("delete from T_Comment where id = ?").setParameter(1, id).executeUpdate();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 删除评论的回复
	 * 具体的权限判断由controller控制
	 * @param id
	 * @return
	 */
	public void removeReply(String id) throws Exception {
		try {
			emRead.createNativeQuery("delete from T_CommentReply where id = ?").setParameter(1, id).executeUpdate();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 通过ip获取地址信息
	 * @param ip
	 * @return
	 */
	public String getPlaceByIp(String ip) {
		return IPSeekerFactory.getResource().getArea(ip);
	}

	/**
	 * @param ip
	 * @return
	 */
	public String getReplierInfo(String ip) {
		return "[" + this.getPlaceByIp(ip) + "]" + "网友";
	}

	/**
	 * 获得评论所对应主体的id
	 * @param id
	 * @return
	 */
	public String getPrincipleId(String id) {
		return this.find(id).getPrincipalId();
	}

}
