(ns web-api.components.example-component
  (:require [com.stuartsierra.component :as component]))


(defrecord ExampleComponent
  [config]
  component/Lifecycle

  (start [component]
    (println "Starting exmple component")
    (assoc component :state :started))

  (stop [component]
    (println "Stopping example component")
    (assoc component :state nil)))

(defn new-expample-component
  [config]
  (map->ExampleComponent {:config config}))
