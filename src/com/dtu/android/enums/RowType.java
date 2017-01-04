package com.dtu.android.enums;

public enum RowType
{
	LOADING,								//로딩
	STICKY_SPACE,						//sticky버튼공간
	DIVIDER,									//가름선
	EVENT,									//이벤트정보
	APPLICANT,								//일반회원
	JOINED_USER,							//이벤트참가유저
	EVENT_MIEN,							//이벤트风采
	EVENT_MIEN_COMMENT,			//이벤트风采평론
	EVENT_NOTIFICATION,				//이벤트통지
	EVENT_NOTICE_USER_REPLY,		//이벤트통지유저회답
	COMMENT,								//기업/일반회원이 받은 평가
	COMMENT_EMPLOYER,				//기업회원에 대한 평가
	EMPLOYER_APPLICANT,				//일반회원에 대한 평가
	CHAT_CONTENT,						//활동내에서 채팅내용
	SYSTEM_NOTICE,						//체계통지
	REQ_JOIN,								//참가신청유저(통지)
	NOTICE_APPLICANT_WORK,			//일반회원작업통지(통지)
}
