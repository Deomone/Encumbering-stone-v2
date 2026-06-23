package com.example.encumberingstone.component;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record EncumberingStoneComponent(boolean enabled, int capacity, List<Identifier> exceptions) {

	public static final Codec<EncumberingStoneComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.optionalFieldOf("enabled", false).forGetter(EncumberingStoneComponent::enabled),
			Codec.INT.optionalFieldOf("capacity", 9).forGetter(EncumberingStoneComponent::capacity),
			Identifier.CODEC.listOf().optionalFieldOf("exceptions", List.of()).forGetter(EncumberingStoneComponent::exceptions)
	).apply(instance, EncumberingStoneComponent::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, EncumberingStoneComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, EncumberingStoneComponent::enabled,
			ByteBufCodecs.VAR_INT, EncumberingStoneComponent::capacity,
			Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), EncumberingStoneComponent::exceptions,
			EncumberingStoneComponent::new
	);

	public EncumberingStoneComponent {
		exceptions = List.copyOf(exceptions);
	}

	public static EncumberingStoneComponent createDefault(int capacity) {
		return new EncumberingStoneComponent(false, capacity, List.of());
	}

	public boolean contains(Identifier itemId) {
		return exceptions.contains(itemId);
	}

	public boolean isFull() {
		return exceptions.size() >= capacity;
	}

	public EncumberingStoneComponent toggled() {
		return new EncumberingStoneComponent(!enabled, capacity, exceptions);
	}

	public EncumberingStoneComponent withException(Identifier itemId) {
		if (contains(itemId) || isFull()) {
			return this;
		}
		List<Identifier> copy = new ArrayList<>(exceptions);
		copy.add(itemId);
		return new EncumberingStoneComponent(enabled, capacity, copy);
	}

	public EncumberingStoneComponent withoutException(Identifier itemId) {
		if (!contains(itemId)) {
			return this;
		}
		List<Identifier> copy = new ArrayList<>(exceptions);
		copy.remove(itemId);
		return new EncumberingStoneComponent(enabled, capacity, copy);
	}
}
