package com.technoetic.xplanner.domain;


//DEBT(DATADRIVEN) Rename to ActionDef
public class ActionMapping {
    private String name;
    private String titleKey;
    private String permission;
    private String domainType;
    private String targetPage;
    private String iconPath;
    private boolean passOidParam;
    private String confirmationKey;
    private boolean backToParent;

    public ActionMapping(String name,
                         String titleKey,
                         String permission,
                         String iconPath,
                         String targetPage,
                         String domainType,
                         boolean backToParent,
                         boolean passOidParam,
                         String confirmationKey
    ){
       this.name = name;
       this.titleKey = titleKey;
       this.permission = permission;
       this.iconPath = iconPath;
       this.targetPage = targetPage;
       this.domainType = domainType;
       this.confirmationKey = null;
       this.backToParent = backToParent;
       this.passOidParam = passOidParam;
       this.confirmationKey = confirmationKey;
    }

    public ActionMapping (String action,
                          String actionKey,
                          String permission,
                          String iconPath,
                          String targetPage,
                          String domainType,
                          boolean backToParent){
       this(action, actionKey, permission, iconPath, targetPage, domainType, backToParent, true, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }

    public String getDomainType() {
        return domainType;
    }

    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getConfirmationKey() {
        return confirmationKey;
    }

    public void setConfirmationKey(String confirmationKey) {
        this.confirmationKey = confirmationKey;
    }

   public boolean isBackToParent()
   {
      return backToParent;
   }

   public void setBackToParent(boolean backToParent)
   {
      this.backToParent = backToParent;
   }

   public String getTitleKey()
   {
      return titleKey;
   }

   public void setTitleKey(String titleKey)
   {
      this.titleKey = titleKey;
   }

   public boolean shouldPassOidParam() {
      return passOidParam;
   }

   public void setPassOidParam(boolean passOidParam) {
      this.passOidParam = passOidParam;
   }

   public boolean isVisible(Nameable object){return true;}

}
