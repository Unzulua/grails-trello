package grails.trello.domain

/* Represents a board action with the original shape:
       [id:5a3b89831331b356d47131c7, idMemberCreator:4fa8ee6ad63acb72042c8f8c, data:[list:[name:First list, id:5a3940b214fd8aba24433229], board:[shortLink:0RkhOB6l, name:for testing purposes, id:5a394064b1171cc47ccce67e], card:[shortLink:eIyNbqCU, idShort:7, name:With due date, id:5a39418780f5b928e9921056, due:2017-12-26T11:00:00.000Z], old:[due:2018-12-26T11:00:00.000Z]], type:updateCard, date:2017-12-21T10:14:27.060Z, memberCreator:[id:4fa8ee6ad63acb72042c8f8c, avatarHash:6e0baf7c6fb6d223ee78f6b37a92d83c, fullName:Jesús Jiménez Ballano, initials:JJB, username:jjballano]]
*/
class Action {
    ActionType type
    String user
    String cardName
    String project
    Date date
		Map original

    static allowedTypes = ["updateCard", "createCard"]

    static Action from(Map params) {
      new Action(
				user: params.memberCreator.username,
        cardName: params.data.card != null ? params.data.card.name: "",
        project: params.data.list != null? params.data.list.name : "",
				date: Date.parse('yyyy-MM-dd', params.date),
				type: params.type == 'updateCard' ? ActionType.UPDATE_CARD : ActionType.CREATE_CARD,
				original: params
			)

    }

    static java.util.List<Action> fromList(java.util.List rawActions){
      rawActions.findAll{allowedTypes.contains(it.type)}.collect{ Action.from(it) }
    }

  	String toString(){
		  "${type} ${user}@${project}"
	  }
}

//https://developers.trello.com/reference#action-types
enum ActionType {
    CREATE_CARD,
    UPDATE_CARD
}
