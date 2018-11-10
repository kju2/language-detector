package com.optimaize.langdetect.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MinorityScriptsRemoverTest {

	private static final MinorityScriptsRemover remover = new MinorityScriptsRemover();

	@Test
	void givenATextWithOnlyOneScriptThenNothingIsRemoved() {
		String text = "This is a text in English.";
		assertEquals(text, remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenATextWithAnInheritedCharacterThenNothingIsRemoved() {
		String text = "\u0300: The grave accent ( \u0300 ) is a diacritical mark in many written languages.";
		assertEquals(text, remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenATextWithMorewhiteSpaceThanLatinCharactersThenNothingIsRemoved() {
		String text = "     This     is     a     text     with     a      lot     of     whitespace.    ";
		assertEquals(text, remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenAEnglishTextWithChineseAndTheChineseIsAboveTheThresholdThenNothingIsRemoved() {
		String text = "This is a text in English. 设为首页收藏本站 开启辅助访问";
		assertEquals(text, remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenAnEnglishTextWithSomeCyrillicAndHaniThenTheMinorityScriptsAreRemoved() throws Exception {
		String text = "Hu Jintao (in Chinese 胡錦濤) and Leo Tolstoy (in Russian Лев Николаевич Толстой) are two well known people. Or so I heard.";
		assertEquals("Hu Jintao (in Chinese ) and Leo Tolstoy (in Russian   ) are two well known people. Or so I heard.", remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenAChineseTextWithSomeEnglishThenTheMinorityScriptsAreRemoved() throws Exception {
		String text = "设为首页收藏本站 开启辅助访问 为首页收藏本站 开启辅助访为首页收藏本站 开启辅助访切换到窄版 请 登录 后使用快捷导航 没有帐号 注册 用户名 Email";
		assertEquals("设为首页收藏本站 开启辅助访问 为首页收藏本站 开启辅助访为首页收藏本站 开启辅助访切换到窄版 请 登录 后使用快捷导航 没有帐号 注册 用户名 ", remover.apply(new StringBuilder(text)).toString());
	}

	@Test
	void givenAnEnglishTextWithSomeGreekThenTheMinorityScriptsAreRemoved() throws Exception {
		String text = "Λabc defΛghi jklΛmno pqrΛ";
		assertEquals("abc def ghi jkl mno pqr ", remover.apply(new StringBuilder(text)).toString());
	}
}
