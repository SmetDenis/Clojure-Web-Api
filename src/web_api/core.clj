(ns web-api.core
  (:require [com.stuartsierra.component :as component]
            [web-api.config :as config]
            [web-api.components.example-component :as example-component]
            [web-api.components.pedestal-component :as pedestal-component]))

(defn web-api-system
  [config]
  (component/system-map
    :example-component (example-component/new-expample-component config)
    :pedestal-component (component/using
                          (pedestal-component/new-pedestal-component config) [:example-component])))

(defn -main
  []
  (let [system (-> (config/read-config)
                   (web-api-system)
                   (component/start-system))]
    (println "It's alive")
    (.addShutdownHook
      (Runtime/getRuntime)
      (new Thread #(component/stop-system system)))))
