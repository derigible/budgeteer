/**
 * 
 */
package derigible.utils;

/**
 * @author marphill
 *
 */
public final class StringU {
	
	private StringU(){
		//Does nothing - methods should be static.
	}
	
	/**
	 * Converts a string to a beautified output.  This basically cleans
     * out extra whitespace from the string, and converts it to proper case.
     * If the name is already mixed case, it's assumed that is on purpose, so this
     * method will not convert the capitalization, but still clean up extra whitespace.
     * 
     * Cases:
     * - if the input is null, return null
     * - if the input is a 0-length string, return an empty string
     * - if the input is mixed case (contains upper and lower case), trim out extra spaces
     * - if the input is all upper case or all lower case, capitalize first letter of each word, 
     *   lower case other letters, and trim out extra spaces
     *   
     * Method is quite fast - use it nearly everywhere a name string is output to make it look nice.
     * 
     * Example output:
     * 
		[null] => [null]
		[] => []
		[A] => [A]
		[b] => [B]
		[bob] => [Bob]
		[BOB] => [Bob]
		[McDonald] => [McDonald]
		[a b] => [A B]
		[A B] => [A B]
		[jim jones] => [Jim Jones]
		[JOHN JOE DOE] => [John Joe Doe]
		[Mike McRae] => [Mike McRae]
		[mike McRae] => [mike McRae]
		[  jim  jones] => [Jim Jones]
		[JOHN   JOE DOE] => [John Joe Doe]
		[Mike McRae  ] => [Mike McRae]
		[ mike  McRae ] => [mike McRae]
     *   
	 * @param input - the string to be formatted
	 * @return the formatted string
	 */
	public static String formatNameString(String input) {
        if(input == null){
        	return null;
        } else if(input.length() == 0){
        	return "";
	    } else{
	    	input = input.trim();
	    	
	    	String[] names = input.split(" "); //Split on the spaces in between to test each word for casing individually
	
	    	if (names.length == 1 && input.length() == 1){
	    		return input.toUpperCase(); //Return single character case
	    	}
	    	int is_case = 0; //0 for upper, 1 for lower, 2 for mixed
	        char[] chars = input.toCharArray();
	    	//Check for upper, lower, mixed case, incorrect casing
	    	for (int i = 0; i < chars.length; i++){
	    		if(Character.isUpperCase(chars[i]) && is_case == 1) {
	    			is_case = 2;
	    			break; //Break out of for loop with mixed case input
	    		} else if(Character.isLowerCase(chars[i])){
	    			is_case = 1;
	    		}
	    		//If character was uppercase, do nothing (will return 0 if all are uppercase)
	    	}
	    	input = "";
	    	if(is_case == 0 || is_case == 1){
	    		for(int j=0; j < names.length; j++){
	    			chars = names[j].toCharArray();
	        		for(int k = 0; k < chars.length; k++){
	        			if(k == 0){
	        				chars[k] = Character.toUpperCase(chars[k]);
	        			} else{
	        				chars[k] = Character.toLowerCase(chars[k]);
	        			}
	        		}
	        		if(j==0){
	        			input += new String(chars);
	        		} else{
	        			if (!names[j].isEmpty()){
	        				input +=  " " + new String(chars);
	        			}		        			
	        		}
	    		}
	    	}
	    	if(is_case == 2){
	    		for(int j=0; j < names.length; j++){
	    			if(j==0){
	    				input += new String(names[j]);
	        		} else{
	        			if (!names[j].isEmpty()){
	        				input += " " + new String(names[j]);
	        			}
	        		}
	    		}
	    	}
	    }    
	    return input;   
	}
	
	/**
	 * Does pretty much the same thing as formatNameString with the exception that it treats
	 * all strings, mixed case or not, as poorly formatted and beautifies them.
	 * 
	 * Example output:
	 * 
	 *  [null] => [null]
		[] => []
		[ ] => []
		[A] => [A]
		[b] => [B]
		[bob] => [Bob]
		[BOB] => [Bob]
		[McDonald] => [Mcdonald]
		[a b] => [A B]
		[A B] => [A B]
		[jim jones] => [Jim Jones]
		[JOHN JOE DOE] => [John Joe Doe]
		[Mike McRae] => [Mike Mcrae]
		[mike McRae] => [Mike Mcrae]
		[  jim  jones] => [Jim Jones]
		[JOHN   JOE DOE] => [John Joe Doe]
		[Mike McRae  ] => [Mike Mcrae]
		[ mike  McRae ] => [Mike Mcrae]

	 * 
	 * @param input - the string to be formatted
	 * @return the formatted string
	 */
	public static String formatString(String input){
		if(input == null){
        	return null;
        } else if(input.length() == 0){
        	return "";
        } else{
	    	input = input.trim();
	    	
	    	String[] names = input.split(" ");
	
	    	if (names.length == 1 && input.length() == 1){
	    		return input.toUpperCase(); //Return single character case
	    	}
	    	input = "";
	        char[] chars = input.toCharArray();
			for(int j=0; j < names.length; j++){
				chars = names[j].toCharArray();
				if (!names[j].isEmpty()){
					chars[0] = Character.toUpperCase(chars[0]);
		    		for(int k = 1; k < chars.length; k++){
		    			chars[k] = Character.toLowerCase(chars[k]);
		    		}
		    		if(j==0){
		    			input += new String(chars);
		    		} else{
	    				input +=  " " + new String(chars);
	    			}		        			
	    		}
			}
        }
		return input;
	}
	
	/**
	 * Takes a string input and cleans up the whitespace and returns a lower case version of the
	 * string. Returns null if string is empty.
	 * 
	 * @param input - the string to format
	 * @return the formatted string
	 */
	public static String lower(String input){
		if(input == null){
        	return null;
        }
    	if(input.isEmpty()){
    		return null;
    	}
		input = input.trim();
    	String[] names = input.split(" ");
    	if (names.length == 1 && input.length() == 1){
    		return input.toLowerCase(); //Return single character case
    	}
    	input = "";
        char[] chars = input.toCharArray();
		for(int j=0; j < names.length; j++){
			chars = names[j].toCharArray();
			if (!names[j].isEmpty()){
	    		for(int k = 0; k < chars.length; k++){
	    			chars[k] = Character.toLowerCase(chars[k]);
	    		}
	    		if(j==0){
	    			input += new String(chars);
	    		} else{
    				input +=  " " + new String(chars);
    			}		        			
    		}
		}
		return input;
	}

    /**
     * Test the formatNameString method and report the success/failure.
     */
    private static int test(String input, String expected) {
        String output = lower(input);
        if (expected == null && output == null) {
            System.out.println("SUCCESS: ["+input+"] => ["+output+"]");
            return 0;
        } else if (expected == null && output != null) {
            // error
        } else if (!expected.equals(output)) {
            // error
        } else {
            System.out.println("SUCCESS: ["+input+"] => ["+output+"]");
            return 0;
        }
        System.out.println("ERROR:   ["+input+"] => ["+output+"] *** SHOULD BE ["+expected+"]");
        return 1;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int errors = 0;
        
        // test null input
        errors += test(null, null);
        
        // test empty input
        errors += test("", null);
        
        // test space input
        errors += test(" ", "");
        
        //Test multiple space input
        errors += test("      ", "");
        
        //Test all others
        errors += test(" a", "a");
        errors += test(" Ba", "ba");
        errors += test("b A ", "b a");
        errors += test("bb ", "bb");
        
//        // test single character input
//        errors += test("A", "A");
//        errors += test("b", "B");
//        
//        // test single name input
//        errors += test("bob", "Bob");
//        errors += test("BOB", "Bob");
//        errors += test("McDonald", "Mcdonald");
//        
//        // test multiple character input
//        errors += test("a b", "A B");
//        errors += test("A B", "A B");
//        
//        // test multiple name input
//        errors += test("jim jones", "Jim Jones");
//        errors += test("JOHN JOE DOE", "John Joe Doe");
//        errors += test("Mike McRae", "Mike Mcrae");
//        errors += test("mike McRae", "Mike Mcrae");
//
//        // test trimming extra whitespace
//        errors += test("  jim  jones", "Jim Jones");
//        errors += test("JOHN   JOE DOE", "John Joe Doe");
//        errors += test("Mike McRae  ", "Mike Mcrae");
//        errors += test(" mike  McRae ", "Mike Mcrae");
        
        System.out.println();
        if (errors > 0) System.out.println("There were "+errors+" error(s).");
        else System.out.println("All tests pass.");

	}

}
