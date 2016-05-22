package com.server.game.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.server.Utils.XFLog;
import com.server.game.data.BaseConfig;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

//52张扑克牌对应的数字
//0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,	//方块 A - K
//0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,	//梅花 A - K
//0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x2B,0x2C,0x2D,	//红桃 A - K
//0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x3A,0x3B,0x3C,0x3D,	//黑桃 A - K
enum CardType {
	UNKNOW,E_DL, E_BZ,E_SJ,E_JH,E_SZ,E_DZ,E_GP,
}
public class CardUtils {
	private final float dl = BaseConfig.getInstance().GLDL;
	private final float bz = BaseConfig.getInstance().GLBZ+dl;
	private final float sj = BaseConfig.getInstance().GLSJ+bz;
	private final float jh = BaseConfig.getInstance().GLJH+sj;
	private final float sz = BaseConfig.getInstance().GLSJ+jh;
	private final float dz = BaseConfig.getInstance().GLDZ+sz;
	private final float gp = BaseConfig.getInstance().GLGP-dz;
	private ArrayList<Integer> cards_ = new ArrayList<>();
	public CardUtils() {
		
	}
	public ArrayList<Integer> getDLCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getDLCards not engouth cards");
			
			return cards;
		}
		for (int i = 0; i < cards_.size(); i++) {
			if ((int)(cards_.get(i)&0x0F) == 2
				|| (int)(cards_.get(i)&0x0F) == 3
				|| (int)(cards_.get(i)&0x0F) == 5) {
				cards.add(cards_.get(i));
			}
		}
		if (cards.size() < 3) {
			cards.clear();
			return cards;
		}
		for (int i = 0; i < cards.size()-2; i++) {
			for (int j = i+1; j < cards.size()-1; j++) {
				for (int k = j+1; k < cards.size(); k++) {
					ArrayList<Integer> mCards = new ArrayList<>();
					mCards.add(cards.get(i));
					mCards.add(cards.get(j));
					mCards.add(cards.get(k));
					if (isDLCard(mCards)) {
						for (int l = 0; l < mCards.size(); l++) {
							removeArryListValue(cards_, mCards.get(l));
						}
						return mCards;
					}
				}
			}
		}
		cards.clear();
		return cards;
	}
	public ArrayList<Integer> getBZCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getBZCards not engouth cards");
			return cards;
		}
		int i = 0;
		for (; i < (cards_.size()-2); i++) {
			cards.clear();
			cards.add(cards_.get(i));
			for (int j = i+1; j < cards_.size(); j++) {
				if ((int)(cards_.get(i)&0x0F) == (int)(cards_.get(j)&0x0F)) {
					cards.add(cards_.get(j));
					if (3 == cards.size()) {
						break;
					}
				}
			}
			if (3 == cards.size()) {
				break;
			}			
		}
		if (3 == cards.size()) {
			for (i = 0; i < cards.size(); i++) {
				removeArryListValue(cards_, cards.get(i));
			}
		}else {
			cards.clear();
		}		
		return cards;
	}
	public ArrayList<Integer> getSJCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getSJCards not engouth cards");
			return cards;
		}
		int i = 0;
		for (; i < (cards_.size()-2); i++) {
			ArrayList<Integer> usefulCards = new ArrayList<>();
			int iCardValue = cards_.get(i);
			int iCardType = (int)((byte)iCardValue & 0xF0) >> 4;
			for (int j = i+1; j < cards_.size(); j++) {
				int jCardValue = cards_.get(j);
				int jCardType = (int)((byte)jCardValue & 0xF0) >> 4;
				if (iCardType == jCardType && (Math.abs(iCardValue-jCardValue) <=2 || Math.abs(iCardValue-jCardValue)>=11)) {
					usefulCards.add(cards_.get(j));
				}
			}
			if (usefulCards.size() < 2) {
				continue;
			}else {
				for (int j = 0; j < usefulCards.size()-1; j++) {
					for (int j2 = j+1; j2 < usefulCards.size(); j2++) {
						cards.add(iCardValue);
						cards.add(usefulCards.get(j));
						cards.add(usefulCards.get(j2));
						if(isSJCard(cards)){
							removeArryListValue(cards_,iCardValue);
							removeArryListValue(cards_,usefulCards.get(j));
							removeArryListValue(cards_,usefulCards.get(j2));
							return cards;
						}else{
							cards.clear();
						}
					}
				}
			}
		}
		return cards;
	}
	public ArrayList<Integer> getJHCards(){
		ArrayList<Integer> sameTypeCards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getJHCards not engouth cards");
			return sameTypeCards;
		}
		for (int i = 0; i < cards_.size()-2; i++) {
			int iCardValue = cards_.get(i);
			int iCardType = (int)((byte)iCardValue & 0xF0) >> 4;
			sameTypeCards.add(cards_.get(i));
			for (int j = i+1; j < cards_.size(); j++) {
				int jCardValue = cards_.get(j);
				int jCardType = (int)((byte)jCardValue & 0xF0) >> 4;
			 	if (iCardType == jCardType) {
					sameTypeCards.add(cards_.get(j));
					if (3 == sameTypeCards.size()) {
						break;
					}
				}
			}
			if (3 == sameTypeCards.size()) {
				for (int k = 0; k < sameTypeCards.size(); k++) {
					removeArryListValue(cards_, sameTypeCards.get(k));
				}
				break;
			}else{
				sameTypeCards.clear();
			}
		}
		return sameTypeCards;
	}

	public ArrayList<Integer> getSZCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getSZCards not engouth cards");
			return cards;
		}
		int i = 0;
		ArrayList<Integer> usefulCards = new ArrayList<>();
		for (; i < (cards_.size()-2); i++) {
			usefulCards.clear();
			int iCardValue = cards_.get(i);
			for (int j = i+1; j < cards_.size(); j++) {
				int jCardValue = cards_.get(j);
				if ((Math.abs(iCardValue-jCardValue) <=2 || Math.abs(iCardValue-jCardValue)>=11)) {
					usefulCards.add(cards_.get(j));
				}
			}
			if (usefulCards.size() < 2) {
				continue;
			}else {
				for (int j = 0; j < usefulCards.size()-1; j++) {
					for (int j2 = j+1; j2 < usefulCards.size(); j2++) {
						cards.add(iCardValue);
						cards.add(usefulCards.get(j));
						cards.add(usefulCards.get(j2));
						if(isSZCard(cards)){
							removeArryListValue(cards_,iCardValue);
							removeArryListValue(cards_,usefulCards.get(j));
							removeArryListValue(cards_,usefulCards.get(j2));
							return cards;
						}else{
							cards.clear();
						}
					}
				}
			}
		}
		return cards;
	}
	public ArrayList<Integer> getDZCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getDZCards not engouth cards");
			return cards;
		}
		for (int i = 0; i < cards_.size()-2; i++) {
			cards.clear();
			cards.add(cards_.get(i));
			for (int j = i+1; j < cards_.size(); j++) {
				if ((int)(cards_.get(i)&0x0F) == (int)(cards_.get(j)&0x0F) ) {
					cards.add(cards_.get(j));
					for (int k = 0; k < cards_.size(); k++) {
						if (k!= i && k!=j) {
							int kCard = cards_.get(k);
							cards.add(kCard);
							if (!isBZCard(cards)) {
								for (int l = 0; l < cards.size(); l++) {
									removeArryListValue(cards_, cards.get(l));
								}
								return cards;
							}else{
								removeArryListValue(cards, kCard);
							}
						}
					}
					break;
				}
			}
		}
		cards.clear();
		return cards;
	}
	public ArrayList<Integer> getGPCards(){
		ArrayList<Integer> cards = new ArrayList<>();
		if (cards_.size() < 3) {
			XFLog.out("error error error error error error error error error getDZCards not engouth cards");
			return cards;
		}
		for (int i = 0; i < cards_.size()-2; i++) {
			for (int j = i+1; j < cards_.size()-1; j++) {
				for (int k = j+1; k < cards_.size(); k++) {
					cards.clear();
					cards.add(cards_.get(i));
					cards.add(cards_.get(j));
					cards.add(cards_.get(k));
					if (!isDLCard(cards) && !isBZCard(cards) && !isSameTypeCard(cards) && !isSZCard(cards) && !isDZCard(cards)) {
						for (int l = 0; l < cards.size(); l++) {
							removeArryListValue(cards_, cards.get(l));
						}
						return cards;
					}
				}
			}
		}
		cards.clear();
		return cards;
	}

	
	
	/////////////////////################### 判断牌型   #########################////////////////////////////////
	public boolean isDLCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isDLCard cards num is error");
			return false;
		}
		ArrayList<Integer> cards = new ArrayList<>();
		for (int i = 0; i < pCard.size(); i++) {
			cards.add((int)(pCard.get(i) & 0x0F));
		}
		for (int i = 0; i < cards.size()-1; i++) {
			for (int j = i+1; j < cards.size(); j++) {
				if (cards.get(i) > cards.get(j)) {
					int temp = cards.get(i);
					cards.set(i, cards.get(j));
					cards.set(j, temp);
				}
			}
		}
		if (cards.get(0) != 2 || cards.get(1) != 3 || cards.get(2) != 5) {
			return false;
		}
		int a = (int)((byte)(pCard.get(0)&0xF0) >> 4);
		int b = (int)((byte)(pCard.get(1)&0xF0) >> 4);
		int c = (int)((byte)(pCard.get(2)&0xF0) >> 4);
		if (a==b && a==c) {
			return false;
		}
		XFLog.out("card : "+pCard.get(0)+" "+pCard.get(1)+" "+pCard.get(2)+" is dilong");
		return true;
	}
	public boolean isBZCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isBZCard cards num is error");
			return false;
		}
		for (int i = 1; i < pCard.size(); i++) {
			if ((int)(pCard.get(0)&0x0F) != (int)(pCard.get(i)&0x0F)) {
				return false;
			}
		}
		return true;
	}
	public boolean isSJCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isSJCard cards num is error");
			return false;
		}
		if (!isSameTypeCard(pCard)) {
			return false;
		}
		
		ArrayList<Integer> cards = new ArrayList<>();
		for (int i = 0; i < pCard.size(); i++) {
			cards.add( (int)(pCard.get(i) & 0x0F) );
		}
		for (int i = 0; i < cards.size()-1; i++) {
			for (int j = i+1; j < cards.size(); j++) {
				if (cards.get(i) > cards.get(j)) {
					int temp = cards.get(i);
					cards.set(i, cards.get(j));
					cards.set(j, temp);
				}
			}
		}
		int i = cards.get(0);
		int j = cards.get(1);
		int k = cards.get(2);
		if ((i+1 == j && j+1 ==k) || (i==1 && j== 12&& k == 13)) {
			return true;
		}
		return false;
	}
	public boolean isJHCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isJHCard cards num is error");
			return false;
		}
		if (isSameTypeCard(pCard) && !isSJCard(pCard) ) {
			return true;
		}
		return false;
	}
	public boolean isSZCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isSZCard cards num is error");
			return false;
		}
		if (isSameTypeCard(pCard)) {
			return false;
		}

		ArrayList<Integer> cards = new ArrayList<>();
		for (int i = 0; i < pCard.size(); i++) {
			cards.add( (int)(pCard.get(i) & 0x0F) );
		}
		for (int i = 0; i < cards.size()-1; i++) {
			for (int j = i+1; j < cards.size(); j++) {
				if (cards.get(i) > cards.get(j)) {
					int temp = cards.get(i);
					cards.set(i, cards.get(j));
					cards.set(j, temp);
				}
			}
		}
		int i = cards.get(0);
		int j = cards.get(1);
		int k = cards.get(2);
		if ((i+1 == j && j+1 ==k) || (i==1 && j== 12&& k == 13)) {
			return true;
		}
		return false;
	}
	public boolean isDZCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isDZCard cards num is error");
			return false;
		}
		int a = (int)(pCard.get(0)&0x0F);
		int b = (int)(pCard.get(1)&0x0F);
		int c = (int)(pCard.get(2)&0x0F);
		if ((a==b || a==c || b==c) && !isBZCard(pCard)) {
			return true;
		}
		return false;
	}
	public boolean isGPCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isGPCard cards num is error");
			return false;
		}
		if (!isDLCard(pCard) && !isBZCard(pCard) && !isSameTypeCard(pCard) && !isSZCard(pCard) && !isDZCard(pCard)) {
			return true;
		}
		return false;
	}
	
	
	////////////////////###########################   common function       ##############################////////////////////////////////////////
	public CardType getRandomCardType(){
		float random = (float) (Math.random()*100);
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
	public ArrayList<Integer> getRandomCards(){
		CardType type = getRandomCardType();
		ArrayList<Integer> cards = null;
		switch (type) {
		case E_DL:
			cards = getDLCards();
			break;
		case E_BZ:
			cards = getBZCards();
			break;
		case E_SJ:
			cards = getSJCards();
			break;
		case E_JH:
			cards = getJHCards();
			break;
		case E_SZ:
			cards = getSZCards();
			break;
		case E_DZ:
			cards = getDZCards();
			break;
		case E_GP:
			cards = getGPCards();
			break;
		default:
			break;
		}
		if (cards.size() != 3) {
			cards = getRandomCards();
		}
		return cards;
	}
	public void reSetcards(){
		cards_.clear();
		for (int i = 1; i < 62; i++) {
			if (14 == i || 15==i || 16==i || 30==i || 31==i || 32==i || 46==i || 47==i || 48==i) {
				continue;
			}
			cards_.add(i);
		}
//		Random r=new Random();
//		for (int i = 0; i < 52; i++) {
//			int random = r.nextInt(52);
//			if (i != random) {
//				int temp = cards_.get(i);
//				cards_.set(i, cards_.get(random));
//				cards_.set(random, temp);
//			}
//		}
		java.util.Collections.shuffle(cards_);
		XFLog.out("resetCard:");
		for (int i = 0; i < 52; i++) {
			System.out.print(cards_.get(i)+" ");;
		}
		System.out.println();
	}
	private void removeArryListValue(ArrayList<Integer> cards,int value){
		for (int i = 0; i < cards.size(); i++) {
			if (value == cards.get(i)) {
				cards.remove(i);
				break;
			}
		}
	}
	public CardType getCardsType(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error getCardsType cards num is error");
			return CardType.UNKNOW;
		}

		return CardType.UNKNOW;
	}
	public boolean isSameTypeCard(ArrayList<Integer> pCard){
		if (pCard.size() != 3) {
			XFLog.out("error error error error error error error error error isSameTypeCard cards num is error");
			return false;
		}
		int iType = (int)(byte)(pCard.get(0)&0xF0>>4);
		if (iType == (int)(byte)(pCard.get(1)&0xF0>>4) && iType == (int)(byte)(pCard.get(2)&0xF0>>4)) {
			return true;
		}
		return false;
	}
	public ArrayList<Integer> sortCardsSmallBig(ArrayList<Integer> pCards){
		for (int i = 0; i < pCards.size()-1; i++) {
			for (int j = i+1; j < pCards.size(); j++) {
				int iCard = (int)(pCards.get(i) & 0x0F);
				int jCard = (int)(pCards.get(j) & 0x0F);
				iCard = 1==iCard ? 14 : iCard;
				jCard = 1==jCard ? 14 : jCard;
				if (iCard > jCard) {
					int temp = pCards.get(i);
					pCards.set(i, pCards.get(j));
					pCards.set(j, temp);
				}
			}
		}
		return pCards;
	}	
}
