package com.denux.perry.bot.properties;

public class ConfigInt extends com.denux.perry.bot.properties.ConfigElement {
    private int value;

    public ConfigInt(String entryname) {
        super(entryname);
        if(this.isRegisteredInConfig()){
            try{
                this.value = Integer.parseInt(this.load());
            }catch (Exception e){
                System.out.println("Failed whilst loading: " + entryname);
                e.printStackTrace();
            }
        }else{
            try{
                this.save(String.valueOf(0));
            }catch (Exception e){
                System.out.println("Failed whilst saving: " + entryname);
                e.printStackTrace();
            }
        }
    }
    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        try{
            this.save(String.valueOf(value));
        }catch (Exception e){
            System.out.println("Failed whilst saving: " + entryname);
            e.printStackTrace();
        }
    }

}
