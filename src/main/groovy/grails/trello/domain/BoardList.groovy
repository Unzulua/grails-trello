package grails.trello.domain

class BoardList {

    String id
    String name

    static BoardList from(Map params) {
        new BoardList(params)
    }

    def propertyMissing(name, value) {
        // nothing
    }
}
