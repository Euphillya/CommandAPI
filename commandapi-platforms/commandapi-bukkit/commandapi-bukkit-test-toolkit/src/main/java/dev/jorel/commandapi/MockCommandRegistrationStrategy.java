package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.arguments.Argument;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * An implementation of {@link CommandRegistrationStrategy} that handles registering commands in a testing environment.
 */
public class MockCommandRegistrationStrategy extends CommandRegistrationStrategy<MockCommandSource> {
	private final CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource> commandAPIHandler;
	private final CommandDispatcher<MockCommandSource> dispatcher = new CommandDispatcher<>();

	protected MockCommandRegistrationStrategy(CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource> commandAPIHandler) {
		this.commandAPIHandler = commandAPIHandler;
	}

	@Override
	public CommandDispatcher<MockCommandSource> getBrigadierDispatcher() {
		return dispatcher;
	}

	@Override
	public LiteralCommandNode<MockCommandSource> registerCommandNode(LiteralArgumentBuilder<MockCommandSource> builder, String namespace) {
		RootCommandNode<MockCommandSource> root = dispatcher.getRoot();
		LiteralCommandNode<MockCommandSource> node = builder.build();

		root.addChild(node);
		root.addChild(commandAPIHandler.namespaceNode(node, namespace));

		return node;
	}

	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<MockCommandSource> resultantNode, List<LiteralCommandNode<MockCommandSource>> aliasNodes) {
		// Nothing to do
	}

	///////////////////////////
	// UNIMPLEMENTED METHODS //
	///////////////////////////

	@Override
	public void runTasksAfterServerStart() {
		throw new UnimplementedMethodException();
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		throw new UnimplementedMethodException();
	}

	@Override
	public void preReloadDataPacks() {
		throw new UnimplementedMethodException();
	}
}
