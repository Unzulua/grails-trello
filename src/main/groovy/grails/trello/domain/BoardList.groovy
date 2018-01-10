package grails.trello.domain

//[id:5a3940b214fd8aba24433229, name:First list, closed:false, idBoard:5a394064b1171cc47ccce67e, pos:65535]
class BoardList {

    String id
    String name
    String original

    static BoardList from(Map params) {
        BoardList boardList = new BoardList(params)
        boardList.original = params
    }

    static BoardList buildNull(){
        new BoardList(id: "", name: "")
    }

    def propertyMissing(name, value) {
        // nothing
    }
}
