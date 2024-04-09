package org.fairdo.benchmark.api;

// TODO: Super-generic KeyType/ValueType is not that useful, 
// but forcing String seems a bit harsh. What is a reasonable assumption?
public record FDOAttribute<K,V>(K key, V value) {}
