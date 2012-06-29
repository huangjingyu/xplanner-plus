package com.technoetic.xplanner.forms;



public class ImportPeopleForm extends ImportForm {
   public void addResult(String id, String login, String name, String status) {
        PeopleImportResult result = new PeopleImportResult();
        result.setId(id);
        result.setLoginId(login);
        result.setName(name);
        result.setStatus(status);
        result.setLineNbr(this.results.size() + 1);
        this.results.add(result);
    }

}
