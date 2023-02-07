package assign1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;

import assign1.HuffmanEncoderDecoder.Huffmanvertex;

import java.util.BitSet;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanBetterEnDe extends HuffmanEncoderDecoder
{
	public static class HuffmanvertexStr implements Comparable<HuffmanvertexStr>, java.io.Serializable
	{
		public int numberOfChar;
		public String character;
		public HuffmanvertexStr right = null ;//maybe not need to be null.
		public HuffmanvertexStr left = null;
		
		public int compareTo(HuffmanvertexStr notThis ) {
			return this.numberOfChar - notThis.numberOfChar;
		}
		
		public boolean isNullTree() {
			if (right == null && left == null)
				return true;
			return false;
		}
	}
	
	public static HuffmanvertexStr makeTreeStr (Map<String, Integer> mapHuffman)
	{	
		PriorityQueue<HuffmanvertexStr> huffmanQueue = new PriorityQueue<HuffmanvertexStr>();
	    
		for (String key : mapHuffman.keySet()) {
			HuffmanvertexStr hv = new HuffmanvertexStr();
			hv.character = key;
			hv.numberOfChar = mapHuffman.get(key);
			huffmanQueue.add(hv);
		}
		
		while(huffmanQueue.size() > 1)
		{
			HuffmanvertexStr minimum1 = huffmanQueue.remove();
			HuffmanvertexStr minimum2 = huffmanQueue.remove();
			HuffmanvertexStr mergeHV = new HuffmanvertexStr();
			mergeHV.numberOfChar = minimum1.numberOfChar + minimum2.numberOfChar;
			mergeHV.left = minimum1;
			mergeHV.right = minimum2;
			huffmanQueue.add(mergeHV);
		}
		
		return huffmanQueue.remove();
	}

	public static void getCodeStr (HuffmanvertexStr tree, Map<String, String> codes, String recStr )
	{
		if (tree.left == null && tree.right == null)
		{
			codes.put(tree.character,recStr);
		}
		if (tree.left != null || tree.right != null)
		{
			getCodeStr (tree.left, codes, recStr + '0');
			getCodeStr (tree.right, codes, recStr + '1');
		}
		
	}

	public HuffmanBetterEnDe()
	{	
	}

	public void CompressBetter(String[] input_names, String[] output_names) 	
	{
		// count and return map.
		Map<String, Integer> counterChar = CountOfAppearanceStr(input_names, output_names);
		Map<String, Integer> counterStr = CountOfAppearanceStr2(counterChar,input_names, output_names);
		
		Map<String, Integer> counterAll = new HashMap<>();
		counterAll.putAll(counterChar);
		counterAll.putAll(counterStr);
		System.out.println(counterAll);
		// build tree
		HuffmanvertexStr tree = makeTreeStr(counterAll);
		// map of codes
		Map<String, String> codes = new HashMap<>();
		getCodeStr(tree, codes, "");
		System.out.println(codes);
		writeTreeFileStr(tree, output_names);
		// compress
		writeOutputFileStr(input_names, output_names, codes);
	}

	public static HuffmanvertexStr readTreeFileBetter(String[] output_names) {
		HuffmanvertexStr tree = new HuffmanvertexStr();
		try {
			FileInputStream f = new FileInputStream(output_names[1]);
			ObjectInputStream s = new ObjectInputStream(f);
			tree = (HuffmanvertexStr) s.readObject();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tree;
	}

	public static void writeTreeFileStr (HuffmanvertexStr tree , String[] output_names)
	{
			try {
				FileOutputStream f = new FileOutputStream(output_names[1]);
				ObjectOutputStream s = new ObjectOutputStream(f);
				s.writeObject(tree);
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	public static void writeOutputFileStr (String[] input_names, String[] output_names, Map<String, String> codes) {
	try {
		FileInputStream input = new FileInputStream(input_names[0]);
		int index = 0;
		int lengthInput = (int) input.getChannel().size();
		String code;
		BitSet huffmanCodeBit = new BitSet(lengthInput);
		
		
		String str1 = "" + (char)input.read();
		String str2 = "" + (char)input.read();
		String total = str1 + str2;
		for (int i = 0; i < lengthInput; i++) {
			
			if (codes.containsKey(total)&&(codes.get(total).length()<=codes.get(str1).length()))
			{				
				code = codes.get(total);
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '1') {
						huffmanCodeBit.set(index );
					}
					index++;
				}
				lengthInput--;
				str1 = "" +(char) input.read();
				str2 = "" + (char)input.read();
				total = str1 + str2;
			}
			else
			{
				code = codes.get(str1);
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '1') {
						huffmanCodeBit.set(index );
					}
					index++;
				}	
				str1 = str2;
				str2 ="" +(char) input.read();
				total = str1 + str2;
			}
		}
	
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(output_names[0]));
		outputStream.writeObject(huffmanCodeBit);
		// output.write(toWrite.toString().getBytes());

	} catch (Exception e) {
		e.printStackTrace();
	}
}
		
	public static void writeTreeFileBetter(HuffmanvertexStr tree, String[] output_names) {
		try {
			FileOutputStream f = new FileOutputStream(output_names[1]);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(tree);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeOutputFileBetter(String[] input_names, String[] output_names, Map<Character, String> codes) {
		try {
			FileInputStream input = new FileInputStream(input_names[0]);

			int index = 0;
			int lengthInput = (int) input.getChannel().size();
			String code;
			BitSet huffmanCodeBit = new BitSet(lengthInput);
			for (int i = 0; i < lengthInput; i++) {
				code = codes.get((char) input.read());
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '1') {
						huffmanCodeBit.set(index );
					}
					index++;
				}	
			}

			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(output_names[0]));
			outputStream.writeObject(huffmanCodeBit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
	public void DecompressBetter(String[] input_names, String[] output_names) {
		
		// get the codes for the codes. this based only if we know the original input.
		HuffmanvertexStr tree = new HuffmanvertexStr();
		tree = readTreeFileBetter(output_names);
		// restore original input.
		restoreInputBetter(tree, output_names, input_names);
	}
	
	public static void restoreInputBetter(HuffmanvertexStr tree, String[] binary_names, String[] original_names) {
		BitSet huffmanCodeBit;
		try {
			FileOutputStream to_original = new FileOutputStream(original_names[0]); // get from the output file
			FileInputStream binary = new FileInputStream(binary_names[0]);
			ObjectInputStream s = new ObjectInputStream(binary);
			huffmanCodeBit = (BitSet) s.readObject();

			int index = 0;
			while(huffmanCodeBit.length() > index)
			{	
				HuffmanvertexStr tmp = tree;
				while (!tmp.isNullTree()) 
				{
	                boolean bit = huffmanCodeBit.get(index);
	                index++;
	                if (!bit) 
	                	tmp = tmp.left;
	                else
	                	tmp = tmp.right;
	            }
				for (int i = 0; i < tmp.character.length(); i++) {
					to_original.write(tmp.character.charAt(i));
				}
			}
			binary.close();
			to_original.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static Map<String, Integer> CountOfAppearanceStr2 (Map<String, Integer> counterChar, String[] input_names, String[] output_names)
	{
		Map<String, Integer> countCharFile = new HashMap<>();
		//add by user.
		countCharFile.put(". ", 0);
		countCharFile.put(", ", 0);
		countCharFile.put("th", 0);
		countCharFile.put("an", 0);
		countCharFile.put("no", 0);
		countCharFile.put("be", 0);
		countCharFile.put("am", 0);
		countCharFile.put("gh", 0);
		countCharFile.put("ou", 0);
		countCharFile.put("is", 0);
		
		FileInputStream input;
		FileOutputStream output;
		try
		{
			input = new FileInputStream(input_names[0]);
			output = new FileOutputStream(output_names[0]);
			int first = input.read();
			int second = input.read();
			
			for (int i = 0; i < (int) input.getChannel().size()-1; i++)	// Check only 100 first bytes.
			{
				
				String str = "" + (char) first + (char) second;
				
				if (countCharFile.containsKey(str)) {
					countCharFile.put(str, countCharFile.get(str) + 1);
					for(int j = 0; j < str.length();j++ )
					{
						counterChar.put("" + str.charAt(j), counterChar.get(""+str.charAt(j)) - 1);
					}
				}
				first = second;
				second = input.read();
			
			}
			
			input.close();
			output.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return countCharFile;
	}

	public static Map<String, Integer> CountOfAppearanceStr (String[] input_names, String[] output_names)
	{
		Map<String, Integer> countCharFile = new HashMap<>();
		
		
		FileInputStream input;
		FileOutputStream output;
		try
		{
			input = new FileInputStream(input_names[0]);
			output = new FileOutputStream(output_names[0]);

			for (int i = 0; i < (int) input.getChannel().size(); i++)	// Check only 100 first bytes.
			{
				
				int x = input.read();
				if (!countCharFile.containsKey("" +(char) x)) {
					countCharFile.put("" + (char) x, 0);
				}
				countCharFile.put("" +(char) x, countCharFile.get("" +(char) x) + 1);
			
			}
			
			input.close();
			output.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return countCharFile;
	}

}
	


	
	

