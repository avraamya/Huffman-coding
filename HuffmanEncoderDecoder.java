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

/**
 * Assignment 1
 * Submitted by: 
 * Avraam Yagudaev. 	ID# 209238542
 * Tamir Zehavi. 	ID# 308241298
 */

import base.Compressor;

public class HuffmanEncoderDecoder implements Compressor {
	public static class Huffmanvertex implements Comparable<Huffmanvertex>, java.io.Serializable {
		public int numberOfChar;
		public char character;
		public Huffmanvertex right = null;
		public Huffmanvertex left = null;

		public int compareTo(Huffmanvertex notThis) {
			return this.numberOfChar - notThis.numberOfChar;
		}

		public boolean isNullTree() {
			if (right == null && left == null)
				return true;
			return false;
		}
	}

	public static Huffmanvertex makeTree(Map<Character, Integer> mapHuffman) {
		PriorityQueue<Huffmanvertex> huffmanQueue = new PriorityQueue<Huffmanvertex>();

		for (char key : mapHuffman.keySet()) {
			Huffmanvertex hv = new Huffmanvertex();
			hv.character = key;
			hv.numberOfChar = mapHuffman.get(key);
			huffmanQueue.add(hv);
		}

		while (huffmanQueue.size() > 1) {
			Huffmanvertex minimum1 = huffmanQueue.remove();
			Huffmanvertex minimum2 = huffmanQueue.remove();
			Huffmanvertex mergeHV = new Huffmanvertex();
			mergeHV.numberOfChar = minimum1.numberOfChar + minimum2.numberOfChar;
			mergeHV.left = minimum1;
			mergeHV.right = minimum2;
			huffmanQueue.add(mergeHV);
		}

		return huffmanQueue.remove();
	}

	public static void getCode(Huffmanvertex tree, Map<Character, String> codes, String recStr) {
		if (tree.left == null && tree.right == null) {
			codes.put(tree.character, recStr);
		}
		if (tree.left != null || tree.right != null) {
			getCode(tree.left, codes, recStr + '0');
			getCode(tree.right, codes, recStr + '1');
		}

	}

	public HuffmanEncoderDecoder() {
	}

	public void Compress(String[] input_names, String[] output_names) {
		// count and return map.
		Map<Character, Integer> counterChar = CountOfAppearance(input_names, output_names);
		// build tree
		Huffmanvertex tree = makeTree(counterChar);
		// map of codes
		Map<Character, String> codes = new HashMap<>();
		getCode(tree, codes, "");
		writeTreeFile(tree, output_names);
		// compress
		writeOutputFile(input_names, output_names, codes);
	}

	public static Huffmanvertex readTreeFile(String[] output_names) {
		Huffmanvertex tree = new Huffmanvertex();
		try {
			FileInputStream f = new FileInputStream(output_names[1]);
			ObjectInputStream s = new ObjectInputStream(f);
			tree = (Huffmanvertex) s.readObject();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tree;
	}

	public static void writeTreeFile(Huffmanvertex tree, String[] output_names) {
		try {
			FileOutputStream f = new FileOutputStream(output_names[1]);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(tree);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeOutputFile(String[] input_names, String[] output_names, Map<Character, String> codes) {
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
						huffmanCodeBit.set(index);
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

	public void Decompress(String[] input_names, String[] output_names) {

		{
			Huffmanvertex tree = new Huffmanvertex();
			tree = readTreeFile(output_names);
			restoreInput(tree, output_names, input_names);

		}

	}

	public static void restoreInput(Huffmanvertex tree, String[] binary_names, String[] original_names) {
		BitSet huffmanCodeBit;
		try {
			FileOutputStream to_original = new FileOutputStream(original_names[0]); // get from the output file
			FileInputStream binary = new FileInputStream(binary_names[0]);
			ObjectInputStream s = new ObjectInputStream(binary);
			huffmanCodeBit = (BitSet) s.readObject();
			int index = 0;
			while (huffmanCodeBit.length() > index) {
				Huffmanvertex tmp = tree;
				while (!tmp.isNullTree()) {
					boolean bit = huffmanCodeBit.get(index);
					index++;
					if (!bit)
						tmp = tmp.left;
					else
						tmp = tmp.right;
				}
				to_original.write(tmp.character);
			}

			binary.close();
			to_original.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean exist(String letterCode, Map<Character, String> codes) {

		return codes.containsValue(letterCode);
	}

	@Override
	public byte[] CompressWithArray(String[] input_names, String[] output_names) {
		return null;
	}

	@Override
	public byte[] DecompressWithArray(String[] input_names, String[] output_names) {
		return null;
	}

	public static Map<Character, Integer> CountOfAppearance(String[] input_names, String[] output_names) {
		Map<Character, Integer> countCharFile = new HashMap<>();

		FileInputStream input;
		FileOutputStream output;
		try {
			input = new FileInputStream(input_names[0]);
			output = new FileOutputStream(output_names[0]);

			for (int i = 0; i < (int) input.getChannel().size(); i++) // Check only 100 first bytes.
			{

				int x = input.read();
				if (!countCharFile.containsKey((char) x)) {
					countCharFile.put((char) x, 0);
				}
				countCharFile.put((char) x, countCharFile.get((char) x) + 1);

			}

			input.close();
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return countCharFile;
	}
}