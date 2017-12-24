package com.foozulu.domain

/* Represents a board action with the original shape:
       [id:5a3b89831331b356d47131c7, idMemberCreator:4fa8ee6ad63acb72042c8f8c, data:[list:[name:First list, id:5a3940b214fd8aba24433229], board:[shortLink:0RkhOB6l, name:for testing purposes, id:5a394064b1171cc47ccce67e], card:[shortLink:eIyNbqCU, idShort:7, name:With due date, id:5a39418780f5b928e9921056, due:2017-12-26T11:00:00.000Z], old:[due:2018-12-26T11:00:00.000Z]], type:updateCard, date:2017-12-21T10:14:27.060Z, memberCreator:[id:4fa8ee6ad63acb72042c8f8c, avatarHash:6e0baf7c6fb6d223ee78f6b37a92d83c, fullName:Jesús Jiménez Ballano, initials:JJB, username:jjballano]]
*/
class Activity {
    ActivityType type
    String user
    String cardName
    String project
    Date date
		Map original

    static Activity from(Map params) {
      new Activity(
				user: params.memberCreator.username,
        cardName: params.data.card.name,
        project: params.data.list.name,
				date: Date.parse('yyyy-MM-dd', params.date),
				type: params.type == 'updateCard' ? ActivityType.UPDATE_CARD : ActivityType.CREATE_CARD,
				original: params
			)

    }

	 String toString(){
		 "${user}@${project}"
	 }
}

//https://developers.trello.com/reference#action-types
enum ActivityType {
    CREATE_CARD,
    UPDATE_CARD
}
