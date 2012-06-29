package com.technoetic.xplanner.domain;

//TODO refactor to use Enum
public enum IterationStatus {
	ACTIVE(new Short("0")), INACTIVE(new Short("1"));
	protected final short code;

    public static final String ACTIVE_KEY = "active";
    public static final String INACTIVE_KEY = "inactive";

    public static final String[] KEYS = {ACTIVE_KEY, INACTIVE_KEY};

    private IterationStatus(short code){
        this.code = code;
    }

    public String getKey (){
        return KEYS[code];
    }

    public short toInt() {
       return code;
    }


    public static IterationStatus fromKey(String key) {
       if (key == null) {
          return INACTIVE;
       } else if (ACTIVE_KEY.equals(key)) {
          return ACTIVE;
       } else if (INACTIVE_KEY.equals(key)) {
          return INACTIVE;
       } else {
          throw new RuntimeException("Unknown iteration status key");
       }
    }

	   public String toString() {
	      return getKey();
	   }

	public static IterationStatus fromInt(Short statusShort) {
		for (IterationStatus iterationStatus : values()) {
			if(iterationStatus.code == statusShort){
				return iterationStatus;
			}
		}
		return INACTIVE;
	}

}
