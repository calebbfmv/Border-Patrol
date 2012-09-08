package com.dpajd.ProtectionPlugin.Regions;

import java.util.HashMap;

import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;

public class RegionMessages {

	private Region region;
	
	private HashMap<RegionMessageType,String> messages = new HashMap<RegionMessageType,String>();
	
	public enum RegionMessageType{
		FAREWELL(".Messages.Entry"),
		WELCOME(".Messages.Exit");
		
		private String path;
		
		RegionMessageType(String path){
			this.path = path;
		}
		
		@Override
		public String toString(){
			return this.path;
		}
		
		public static RegionMessageType getType(String type){
			for (RegionMessageType msg : RegionMessageType.values()){
				if (msg.name().equals(type)) return msg;
			}
			return null;
		}
		
		public static RegionMessageType getType(ProtectionType type){
			if (type.isMessageFlag()){
				return getType(type.name());
			}
			return null;
		}
	}
	
	
	public RegionMessages(Region region){
		this.region = region;
	}
	
	public void setMessage(RegionMessageType type, String message){
		messages.put(type, message);
	}
	
	public String getMessage(RegionMessageType type){
		if (messages.containsKey(type)){
			return messages.get(type);
		}else return null;		
	}
	
	public String getAltMessage(RegionMessageType type){
		if (messages.containsKey(type)){
			return messages.get(type).replaceAll("§", "&");
		}else return null;		
	}
	
	public boolean hasMessage(RegionMessageType type){
		return messages.containsKey(type);
	}
	
	public void removeMessage(RegionMessageType type){
		messages.remove(type);
	}
	
	public Region getRegion(){
		return region;
	}
}
