String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

function loadComment(object, id) {
	$.ajax({
		url : 'comment/comment!loadComment.action',
		data : 'principleId=' + id,
		dataType : 'text',
		type : 'post',
		success : function(msg) {
			$(object).next().find(".mycomment").html(msg);
		}
	})
}

function loadCommentReply(object, id) {
	$.ajax({
		url : 'comment/comment!loadCommentReply.action',
		data : 'commentId=' + id,
		dataType : 'text',
		type : 'post',
		success : function(msg) {
			$(object).parent().html(msg);
		}
	})
}

function removeComment(object, id) {
	$.ajax({
		url : 'comment/comment!remove.action',
		data : 'id=' + id,
		dataType : 'text',
		type : 'post',
		success : function(msg) {
			if (msg == "true") {
				$(object).parent().parent().remove();
			} else {
				$.messager.alert(msg);
			}
		}
	})
}

function removeReply(object, id) {
	$.ajax({
		url : 'comment/comment!removeReply.action',
		data : 'principleId=' + id,
		dataType : 'text',
		type : 'post',
		success : function(msg) {
			if (msg == "true") {
				$(object).parent().remove();
			} else {
				$.messager.alert(msg);
			}
		}
	})
}

function isEmail(str) {
	if (str.trim().length != 0) {
		reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if (!reg.test(str)) {
			$.messager.alert("对不起，请输入正确的邮箱地址!");
			return false;
		} else {
			return true;
		}
	} else {
		$.messager.alert("对不起，不能为空!");
		return false;
	}
}

function isEmpty(str) {
	if (str.trim().length <= 0) {
		$.messager.alert("对不起，不能为空!");
		return false;
	} else {
		return true;
	}
}

function isCommentLength(str) {
	if (getRealLength(str) >= 250) {
		$.messager.alert("您输入了"+getRealLength(str)+"个字，但是评论不能超过250个字!");
		return false;
	} else {
		return true;
	}
}

function getRealLength(str) {
	var realLength = 0, len = str.length, charCode = -1;
	for ( var i = 0; i < len; i++) {
		charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128)
			realLength += 1;
		else
			realLength += 2;
	}
	return realLength;
}