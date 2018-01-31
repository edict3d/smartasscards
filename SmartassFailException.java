package smartasscards;

class SmartassFailException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public SmartassFailException(String message){
		super(message);
	}
}

class InvalidSmartassCommandException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public InvalidSmartassCommandException(String message){
		super(message);
	}
}

class InvalidSmartassGameException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public InvalidSmartassGameException(String message){
		super(message);
	}
}
