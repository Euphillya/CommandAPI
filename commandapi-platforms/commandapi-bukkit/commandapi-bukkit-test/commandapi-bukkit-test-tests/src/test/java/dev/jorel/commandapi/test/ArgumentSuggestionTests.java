package dev.jorel.commandapi.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.MathOperationArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class ArgumentSuggestionTests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/
	
	@Test
	void testReplaceSuggestionsConstants() {
		new CommandAPICommand("test")
			.withArguments(new StringArgument("arg").replaceSuggestions(ArgumentSuggestions.strings("cat", "apple", "wolf")))
			.executes((sender, args) -> {
			})
			.register();
		
		Player player = server.addPlayer("APlayer");
		
		// /test
		assertEquals(List.of("apple", "cat", "wolf"), server.getSuggestions(player, "test "));
	}
	
	@Test
	void testIncludeSuggestionsConstants() {
		// Our test has to involve an argument which already has some suggestions, so we'll
		// piggy-back on the MathOperationArgument because its suggestions are constant and
		// are easier to test (despite having a mysterious sort order)
		new CommandAPICommand("test")
			.withArguments(new MathOperationArgument("arg").includeSuggestions(ArgumentSuggestions.strings("^^")))
			.executes((sender, args) -> {
			})
			.register();
		
		Player player = server.addPlayer("APlayer");
		
		// /test
		assertEquals(List.of("%=", "*=", "+=", "-=", "/=", "<", "=", ">", "><", "^^"), server.getSuggestions(player, "test "));
	}
}
