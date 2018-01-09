package grails.trello.domain

import grails.util.Holders

class Card {

    String id
    String name
    Date dueDate
    String description
    String idList
    java.util.List<String> labels

    static Card from(Map params) {
        params.labels = params.labels?.collect{ it.name }
        new Card(params)
    }

    void setDesc(String description){
        this.description = description
    }

    void setDue(String date){
        if (date){
            this.dueDate = Date.parse(defaultDateFormat, date)
        }
    }

    Boolean expireBetween(Date from, Date to) {
        if (!dueDate || !from || dueDate < from){
            return false
        }

        to == null || dueDate <= to
    }

    private String getDefaultDateFormat(){
        Holders.grailsApplication.config.trello.date.format
    }
}
