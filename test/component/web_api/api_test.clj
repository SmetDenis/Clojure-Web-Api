(ns component.web-api.api-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [web-api.core :as core]
            [clj-http.client :as client]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(deftest greeting-test
  (with-system
    [sut (core/web-api-system {:server {:port 8088}})]
    (is (= {:body   "Hello, world!"
            :status 200}
           (->
             (str "http://localhost:" 8088 "/greet")
             (client/get)
             (select-keys [:body :status]))))))

(comment
  [{:id    (random-uuid)
    :name  "My Todo list"
    :items [{:id     (random-uuid)
             :name   "Learn Clojure"
             :status :created
             }]}
   {:id    (random-uuid)
    :name  "Draft list"
    :items []}]
  )

(deftest get-todo-test
  (let [todo-id (random-uuid)]
    (with-system
      [sut (core/web-api-system {:server {:port 8088}})]
      (is (= {:body   "Hello, world!"
              :status 200}
             (->
               (str "http://localhost:" 8088 "/todo/" todo-id)
               (client/get)
               (select-keys [:body :status])))))))
