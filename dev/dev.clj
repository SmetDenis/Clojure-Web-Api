(ns dev
  (:require [com.stuartsierra.component.repl :as component-repl]
            [web-api.core :as core]))

(component-repl/set-init
  (fn [_old-system]
    (core/web-api-system {:server {:port 3000}})))
