(ns unit.web-api.simple-test
  (:require [clojure.test :refer :all]
            [web-api.components.pedestal-component :refer [url-for]]))

(deftest route-test
  (testing "Greet endpoint"
    (is (= "/greet" (url-for :greet))))
  (testing "Get Todo by id endpoint"
    (let [todo-id (random-uuid)]
      (is (= (str "/todo/" todo-id)
             (url-for :get-todo {:path-params {:todo-id todo-id}}))))))
