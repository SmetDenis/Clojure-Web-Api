(ns web-api.core
  (:require [com.stuartsierra.component :as component]
            [web-api.components.example-component :as example-component]
            [web-api.components.in-memory-state-component :as in-memory-state-component]
            [web-api.components.pedestal-component :as pedestal-component]
            [web-api.config :as config]))

(defn web-api-system
  [config]
  (component/system-map
   :example-component (example-component/new-expample-component config)
   :in-memory-state-component (in-memory-state-component/new-in-memory-state-component config)
   :pedestal-component (component/using
                        (pedestal-component/new-pedestal-component config)
                        [:example-component
                         :in-memory-state-component])))

(defn -main
  []
  (let [system (-> (config/read-config)
                   (web-api-system)
                   (component/start-system))]
    (println "It's alive")
    (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))))
