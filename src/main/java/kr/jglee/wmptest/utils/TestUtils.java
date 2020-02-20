package kr.jglee.wmptest.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class TestUtils {
	
	// order배열에 순서를 저장함.
	private static int[] order = new int[256];
	static {
		int v = 0;
		for (int i = '0'; i <= '9'; i++)
			order[i] = v++;
		char sub = 'a' - 'A';
		for (int i = 'A'; i <= 'Z'; i++) {
			order[i] = v++;
			order[i + sub] = v++;
		}	
	}

	// Integer.MAX_VALUE를 넘어가는값은 처리가 불가능.
	// 자바의 배열크기는 int범위내에서 가능하다.
	public static boolean isoverflow(String mod) {
		BigInteger big = new BigInteger(mod);
		BigInteger imax = new BigInteger(Integer.MAX_VALUE + "");
		return big.compareTo(imax) > 0;
	}
	
	public static boolean isempty(String s) {
		return s == null || s.trim().equals("");
	}
	
	private static boolean isnum(char c) {
		return '0' <= c && c <= '9';
	}
	
	private static boolean inrange(char c) {
		boolean islowercase = 'a' <= c && c <= 'z';
		boolean isuppercase = 'A' <= c && c <= 'Z';
		return isnum(c) || islowercase || isuppercase;
	}
	
	// [a-zA-Z0-9]+ 문자만 추출하여 소트
	private static ArrayList<Character> numalpha(String s, boolean rmtag) {
		ArrayList<Character> numalpha = new ArrayList<Character>();
		int isopen = 0;
		for (char c : s.toCharArray()) {
			if (c == '<') isopen++;
			else if (c == '>' && isopen > 0) isopen--; // 열림기호가 앞에 없음에도 닫힘기호 등장시 무시함.
			if (!inrange(c)) continue;
			if (rmtag && isopen > 0) continue;
			numalpha.add(c);
		}
		Collections.sort(numalpha, (a, b) -> order[a] - order[b]);
		return numalpha;
	}
	
	// 첫번째 [a-zA-Z]인덱스를 찾는다.
	private static int upperBound(ArrayList<Character> numalpha) {
		return upperBound(numalpha, 0, numalpha.size(), '9');
	}
	
	private static int upperBound(ArrayList<Character> numalpha, int begin, int end, int key){
		int mid;
		while (begin < end){
			mid = (begin + end) / 2;
			if (numalpha.get(mid) <= key) begin = mid + 1;
			else end = mid;
		}
		return end;
	}
	
	// [a-zA-Z]한번 [0-9]한번씩 저장한다.
	private static char[] shuffle(ArrayList<Character> numalpha) {
		int numlen = TestUtils.upperBound(numalpha);

		char[] result = new char[numalpha.size()];
		int resultIdx = 0;
		
		boolean inserted;
		for (int numIdx = 0, alphaIdx = numlen;;) {
			inserted = false;
			if (alphaIdx < numalpha.size()) {
				inserted = true;
				result[resultIdx++] = numalpha.get(alphaIdx++);
			}
			if (numIdx < numlen) { 
				inserted = true; 
				result[resultIdx++] = numalpha.get(numIdx++);
			}
			if (!inserted) break;
		}
		
		return result;
	}
	
	public static char[] test(String s, boolean rmtag) {
		ArrayList<Character> numalpha = TestUtils.numalpha(s, rmtag);
		return shuffle(numalpha);
	}
	
}