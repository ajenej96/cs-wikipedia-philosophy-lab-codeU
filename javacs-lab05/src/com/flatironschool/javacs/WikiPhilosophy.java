package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.Scanner;
import java.util.regex.Pattern;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	final static String philosophyUrl = "https://en.wikipedia.org/wiki/Philosophy";
	static Deque<String> stack = new ArrayDeque<String>();
	static List<String> urlList = new ArrayList<>();
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);
		for(Element paragraph: paragraphs){
			visit(paragraph);
		}

		for(String urls: urlList){
			System.out.println(urls);
		}
		

	}

	public static void visit(Element paragraph) throws IOException {
		
		String parenthesis;
		Elements links = paragraph.select("a");
		int index = 0;
		Iterable<Node> iter = new WikiNodeIterable(paragraph);

		if(urlList.contains(philosophyUrl)){
			return;
		}
		for (Node node: iter) {
			String line = node.toString();
			Scanner pCheck = new Scanner(line);
			if(links.size() == 0){
				System.out.println("failure: no links");
				return;
			}
			Element link = links.get(index);
			if (node instanceof TextNode) {
				
				parenthesis = pCheck.nextLine();
				pCheck = new Scanner(parenthesis);
				String p = pCheck.findInLine("[(]");
					
				if(p!= null){
					
					//System.out.print(p);
					stack.push(p);
					
				}if(stack.isEmpty()){
						
					String url = link.absUrl("href");
				  	if(urlList.contains(philosophyUrl)){
						
						return;
					}
					if(urlList.contains(url)){
						System.out.println("failure: page already visited");
						return;
					}
					
					urlList.add(url);
					//System.out.println(url);
					if(url.equals(philosophyUrl)){
						System.out.println("success");
						return;
					}
					else{
						Elements paragraphs = wf.fetchWikipedia(url);
						paragraph = paragraphs.get(0);
						links = paragraph.select("a");
						if(links.size() == 0){
						       System.out.println("failure: no links");
							return;
						}
						link = links.get(index);
						
						visit(paragraph);
					}
				}
				String cp = pCheck.findInLine("[)]");
				if(cp != null){
					stack.pop();
					index +=1;
					//System.out.println(index);
				}
					//System.out.print(node);
				
				
			}
        	}

	}
}
