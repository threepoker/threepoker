package com.ddd.threepoker.utils;

import java.util.ArrayList;
import java.util.Random;


//52张扑克牌对应的数字
//0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,	//方块 A - K
//0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,	//梅花 A - K
//0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x2B,0x2C,0x2D,	//红桃 A - K
//0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x3A,0x3B,0x3C,0x3D,	//黑桃 A - K
enum CardType {
    E_DL, E_BZ,E_SJ,E_JH,E_SZ,E_DZ,E_GP
}
public class CardUtils {
	private final double dl = 0.01f;
	private final double bz = 0.09f+dl;
	private final double sj = 0.5f+bz;
	private final double jh = 5f+sj;
	private final double sz = 10f+jh;
	private final double dz = 15f+sz;
	private final double gp = 100f-dz;
	private ArrayList<Integer> cards_ = new ArrayList<>();
	public CardUtils() {
		
	}
	public CardType getRandomCardType(){
		double random = Math.random()*100;
		CardType type;
		if(random<=dl){
			type = CardType.E_DL;
		}else if(random<=bz){
			type = CardType.E_BZ;
		}else if(random<=sj){
			type = CardType.E_SJ;
		}else if(random<=jh){
			type = CardType.E_JH;
		}else if(random<=sz){
			type = CardType.E_SZ;
		}else if(random<=dz){
			type = CardType.E_DZ;
		}else {
			type = CardType.E_GP;
		}
		return type;
	}
	public void getRandomcards_(){
		
	}
	public ArrayList<Integer> getBZCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			System.out.println("error error error error error error error error error getBZCards not engouth cards");
			return cards;
		}
		int i = 0;
		for (; i < (cards_.size()-1); i++) {
			int sameCard = 0;
			for (int j = i+1; j < cards_.size(); j++) {
				if ((int)(cards_.get(i)&0x0F) == (int)(cards_.get(j)&0x0F)) {
					sameCard++;
					if (2 == sameCard) {
						break;
					}
				}
			}
			if (2 == sameCard) {
				break;
			}			
		}
		cards.add(cards_.get(i));
		cards_.remove(i);
		int cardFirst = cards.get(0) & 0x0F;
		for (i = 0; i < cards_.size(); i++) {
			if (cardFirst == (cards_.get(i) & 0x0F)) {
				cards.add(cards_.get(i));
				cards_.remove(i);
				i--;
				if (3 == cards.size()) {
					break;
				}
			}
		}
		
		return cards;
	}
	public void reSetcards_(){
		cards_.clear();
		for (int i = 1; i < 62; i++) {
			if (14 == i || 15==i || 16==i || 30==i || 31==i || 32==i || 46==i || 47==i || 48==i) {
				continue;
			}
			cards_.add(i);
		}
		Random r=new Random();
		for (int i = 0; i < 52; i++) {
			int random = r.nextInt(52);
			if (i != random) {
				int temp = cards_.get(i);
				cards_.set(i, cards_.get(random));
				cards_.set(random, temp);
			}
		}
		System.out.println("resetCard:");
		for (int i = 0; i < 52; i++) {
			System.out.print(cards_.get(i)+" ");;
		}
		
	}
}
