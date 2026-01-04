package com.lld.lld.foodDelivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.lld.foodDelivery.models.Agent;

public class AgentRepository {
    private static AgentRepository instance;
    private Map<String, Agent> agents;

    private AgentRepository() {
        this.agents = new HashMap<>();
    }

    public static synchronized AgentRepository getInstance() {
        if (instance == null) {
            instance = new AgentRepository();
        }
        return instance;
    }

    public void registerAgent(Agent agent) {
        agents.put(agent.getAgentId(), agent);
    }

    public Agent getAgent(String agentId) {
        return agents.get(agentId);
    }

    public List<Agent> getAvailableAgents() {
        return agents.values().stream()
                .filter(Agent::isAvailable)
                .collect(Collectors.toList());
    }

    public Agent findAvailableAgent() {
        return agents.values().stream()
                .filter(Agent::isAvailable)
                .findFirst()
                .orElse(null);
    }
}
