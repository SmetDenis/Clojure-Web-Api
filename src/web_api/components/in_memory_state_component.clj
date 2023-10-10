(ns web-api.components.in-memory-state-component
  (:require [com.stuartsierra.component :as component]))


(defrecord InMemoryStateComponent
  [config]
  component/Lifecycle

  (start [component]
    (println "Starting InMemoryStateComponent")
    (assoc component :state :started))

  (stop [component]
    (println "Stopping InMemoryStateComponent")
    (assoc component :state nil)))

(defn new-in-memory-state-component
  [config]
  (map->InMemoryStateComponent {:config config}))