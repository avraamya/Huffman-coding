package assign1;

public class Main {
	
	//To use the second part - add comments here. And remove comments in the second part
	public static void main(String[] args) {
	
		//Here you need to put the address of the original file
		String[] input_names = {"C:\\Users\\avraam\\Documents\\files-huffman\\smiley.bmp"};
		//Here you have to put the address of the encoded file, and an address for a text file where the dictionary will be
		String[] output_names = {"C:\\Users\\avraam\\Documents\\files-huffman\\out.txt","C:\\Users\\avraam\\Documents\\files-huffman\\outmap.txt"};

		//Activation to the first question
		HuffmanEncoderDecoder objectHuffman = new HuffmanEncoderDecoder();
		objectHuffman.Compress(input_names, output_names);
		objectHuffman.Decompress(input_names, output_names);	
	
}

	
	//Activation for the second question - the previous main must be commented

	/*	
	public static void main(String[] args) {

		//Here you need to put the address of the original file
		String[] input_names = {"PATH\\Original file"};
		//Here you have to put the address of the encoded file, and an address for a text file where the dictionary will be
		String[] output_names = {"PATH\\Decoded file","PATH\\File for the dictionary"};


		HuffmanBetterEnDe objectHuffman = new HuffmanBetterEnDe();
		objectHuffman.CompressBetter(input_names, output_names);
		objectHuffman.DecompressBetter(input_names, output_names);
	}
*/

}
