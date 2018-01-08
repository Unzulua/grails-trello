package grails.trello.domain

import grails.util.Holders

class Card {

    String id
    String name
    Date dueDate
    String description
    String idList

    static Card from(Map params) {
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
