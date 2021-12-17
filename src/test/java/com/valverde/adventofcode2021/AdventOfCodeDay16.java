package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCodeDay16 {

    @Test
    public void task1() throws IOException {
//        final File file = new File("input/day16-input.txt");
        final File file = new File("input/day16-input2.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final String binaryString = hexToBin(txtLines.get(0));
        final List<String> binaryList = new ArrayList<>();
        for (int i = 0; i < binaryString.length(); i++) {
            final String bit = String.valueOf(binaryString.charAt(i));
            binaryList.add(bit);
        }
        final String packetVersion = parsePacketVersion(binaryList);
        final String typeId = parseTypeId(binaryList);
        if (!typeId.equals("4")) {
            final String lengthTypeBit = getLengthTypeBit(binaryList);
            if (lengthTypeBit.equals("0")) {
                int totalLengthInBits = getTotalLengthInBits(binaryList);

            } else {
                int numberOfSubPackets = getNumberOfSubPackets(binaryList);
            }
        }
    }

    private int getNumberOfSubPackets(final List<String> binaryList) {
        return Integer.parseInt(getBinaryString(new ArrayList<>(binaryList.subList(7, 18))), 2);
    }

    private int getTotalLengthInBits(final List<String> binaryList) {
        return Integer.parseInt(getBinaryString(new ArrayList<>(binaryList.subList(7, 22))), 2);
    }

    private String getLengthTypeBit(final List<String> binaryList) {
        return binaryList.get(6);
    }

    private String parseTypeId(final List<String> binaryList) {
        final List<String> bits = new ArrayList<>(binaryList.subList(3, 6));
        bits.add(0, "0");
        return binToHex(getBinaryString(bits));
    }

    private String parsePacketVersion(final List<String> binaryList) {
        final List<String> bits = new ArrayList<>(binaryList.subList(0, 3));
        bits.add(0, "0");
        return binToHex(getBinaryString(bits));
    }

    private String getBinaryString(List<String> bits) {
        StringBuilder bitsString = new StringBuilder();
        for (String s: bits) {
            bitsString.append(s);
        }
        return bitsString.toString();
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day16-input2.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
    }

    private String binToHex(String hex){
        hex = hex.replaceAll("0000", "0");
        hex = hex.replaceAll("0001", "1");
        hex = hex.replaceAll("0010", "2");
        hex = hex.replaceAll("0011", "3");
        hex = hex.replaceAll("0100", "4");
        hex = hex.replaceAll("0101", "5");
        hex = hex.replaceAll("0110", "6");
        hex = hex.replaceAll("0111", "7");
        hex = hex.replaceAll("1000", "8");
        hex = hex.replaceAll("1001", "9");
        hex = hex.replaceAll("1010", "A");
        hex = hex.replaceAll("1011", "B");
        hex = hex.replaceAll("1100", "C");
        hex = hex.replaceAll("1101", "D");
        hex = hex.replaceAll("1110", "E");
        hex = hex.replaceAll("1111", "F");
        return hex;
    }

    private String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }
}
