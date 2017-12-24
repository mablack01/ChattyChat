package server.object;

public class UserMessage {

	public String message, userName, password;
	public Command command;
	
	public Command getCommand(){
		return this.command;
	}
	
	public UserMessage(String s){
		if(s.charAt(0) == '[' && s.charAt(s.length()-1) == ']') this.command = Command.LOGIN;
		if(s.charAt(0) == '!'){ 
			this.command = Command.CMD;
		}
		else this.command = Command.MESSAGE;
		
		switch(command){
		case LOGIN: 
			this.userName = s.substring(1, s.indexOf(",", 1));
    		this.password = s.substring(s.indexOf(",", 1), s.length()-2);
			this.message = null;
		case CMD:
			message = s.substring(1);
		case MESSAGE:
			message = s + "\n";
		default:
			break;
			
		}
	}
	
}
