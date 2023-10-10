(ns component.web-api.api-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [web-api.core :as core]
            [clj-http.client :as client]
            [web-api.components.pedestal-component :refer [url-for]])
  (:import (java.net ServerSocket)))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn sur->url
  [sut path]
  (str/join ["http://localhost:"
             (-> sut :pedestal-component :config :server :port)
             path]))

(defn get-free-local-port
  []
  (with-open [socket (ServerSocket. 0)]
    (.getLocalPort socket)))

(deftest basic-testing-tools-test
  (with-system
    [sut (core/web-api-system {:server {:port (get-free-local-port)}})]
    (is (not (= (get-free-local-port) (get-free-local-port))))
    ))

(deftest greeting-test
  (with-system
    [sut (core/web-api-system {:server {:port (get-free-local-port)}})]
    (is (= {:body   "Hello, world!"
            :status 200}
           (-> (sur->url sut (url-for :greet))
               (client/get)
               (select-keys [:body :status]))))))

(deftest get-todo-test
  (let [todo-id-1 (random-uuid)
        todo-1    {:id    todo-id-1
                   :name  "Todo list name"
                   :items [{:id   (random-uuid)
                            :name "Action item 1"}]}]
    (with-system
      [sut (core/web-api-system {:server {:port (get-free-local-port)}})]
      (reset! (-> sut :in-memory-state-component :state-atom) [todo-1])
      (is (= {:body   (pr-str todo-1)
              :status 200}
             (-> (sur->url sut (url-for :get-todo {:path-params {:todo-id todo-id-1}}))
                 (client/get)
                 (select-keys [:body :status]))))
      (testing "Check body is empty on invalid uuid"
        (is (= {:body   ""
                :status 200}
               (-> (sur->url sut (url-for :get-todo {:path-params {:todo-id (random-uuid)}}))
                   (client/get)
                   (select-keys [:body :status])))))
      )))
