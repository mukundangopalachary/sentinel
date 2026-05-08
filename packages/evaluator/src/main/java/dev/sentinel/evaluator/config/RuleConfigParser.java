package dev.sentinel.evaluator.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.valueobject.Percentage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Parser for deserializing and validating flag rule configurations.
 * 
 * RuleConfigParser reads the JSON configuration stored in FlagRule objects and parses it
 * into type-specific configuration records (GlobalRuleConfig, PercentageRolloutRuleConfig, etc.).
 * Invalid or missing configurations return empty Optionals, allowing graceful fallback.
 * 
 * @author Mukundan Gopalachary
 */
public final class RuleConfigParser {

  private final ObjectMapper objectMapper;

  /**
   * Constructs a new RuleConfigParser with a default ObjectMapper.
   */
  public RuleConfigParser() {
    this(new ObjectMapper());
  }

  /**
   * Constructs a new RuleConfigParser with a custom ObjectMapper.
   * 
   * @param objectMapper the ObjectMapper to use for JSON deserialization
   */
  public RuleConfigParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Parses a FlagRule's configuration into a GlobalRuleConfig.
   * 
   * Expects the rule's config JSON to contain an "enabled" boolean field.
   * 
   * @param rule the flag rule to parse
   * @return Optional containing GlobalRuleConfig if valid, empty Optional if config is invalid or missing
   */
  public Optional<GlobalRuleConfig> parseGlobal(FlagRule rule) {
    JsonNode root = readConfig(rule);
    if (root == null || !root.has("enabled") || !root.get("enabled").isBoolean()) {
      return Optional.empty();
    }

    return Optional.of(new GlobalRuleConfig(root.get("enabled").asBoolean()));
  }

  /**
   * Parses a FlagRule's configuration into a PercentageRolloutRuleConfig.
   * 
   * Expects the rule's config JSON to contain a "percentage" integer field (0-100).
   * 
   * @param rule the flag rule to parse
   * @return Optional containing PercentageRolloutRuleConfig if valid, empty Optional if config is invalid or missing
   */
  public Optional<PercentageRolloutRuleConfig> parsePercentageRollout(FlagRule rule) {
    JsonNode root = readConfig(rule);
    if (root == null || !root.has("percentage") || !root.get("percentage").canConvertToInt()) {
      return Optional.empty();
    }

    int percentageValue = root.get("percentage").asInt();
    return Optional.of(new PercentageRolloutRuleConfig(new Percentage(percentageValue)));
  }

  /**
   * Parses a FlagRule's configuration into a UserTargetRuleConfig.
   * 
   * Expects the rule's config JSON to contain an "includedUserIds" array of strings.
   * Non-textual elements in the array are skipped.
   * 
   * @param rule the flag rule to parse
   * @return Optional containing UserTargetRuleConfig if valid, empty Optional if config is invalid or missing
   */
  public Optional<UserTargetRuleConfig> parseUserTarget(FlagRule rule) {
    JsonNode root = readConfig(rule);
    if (root == null || !root.has("includedUserIds") || !root.get("includedUserIds").isArray()) {
      return Optional.empty();
    }

    List<String> includedUserIds = new ArrayList<>();
    for (JsonNode node : root.get("includedUserIds")) {
      if (node.isTextual()) {
        includedUserIds.add(node.asText());
      }
    }

    return Optional.of(new UserTargetRuleConfig(List.copyOf(includedUserIds)));
  }

  /**
   * Reads and parses the JSON configuration from a FlagRule.
   * 
   * @param rule the flag rule
   * @return the parsed JsonNode, or null if parsing fails (e.g., invalid JSON)
   */
  private JsonNode readConfig(FlagRule rule) {
    try {
      return objectMapper.readTree(rule.configJson());
    } catch (IOException ex) {
      return null;
    }
  }
}
