(ns bangumi.core)

(require '[clj-http.client :as client])
(require '[clojure.data.json :as json])
(require '[clojure.java.shell :as sh])


(defn get-bd-download-link
  "get real download link by taking the share link as argument"
  [url]
  (let [params (->>
                (if (re-find #"wap" url)
                  (client/get url)
                  (->> (client/get url)
                       (:body)
                       (re-find #"(?s)SHARE_ID = \"(\d+?)\".*?UK = \"(\d+?)\"")
                       (drop 1)
                       (zipmap [:shareid :uk])
                       (#(client/get "http://pan.baidu.com/wap/link" {:query-params %}))))
                (:body)
                (re-find #"fs_id\\\":\\\"(\d+?)\\\".*shareid=\"(\d+?)\".*?uk=\"(\d+?)\".*?sign=\"(.*?)\".*?timestamp=\"(\d+?)\"")
                (drop 1)
                (zipmap [:fid_list :shareid :uk :sign :timestamp])
                ((fn [map] (update-in map [:fid_list] #(str "[" % "]"))))
                (#(assoc % :r (Math/random))))
        response (->>
                  (client/get "http://pan.baidu.com/share/download"
                              {:query-params params})
                  (:body)
                  (json/read-str))]
    (->> (if (= (get response "errno") 0)
           (get response "dlink")
           (let [vcode (get response "vcode")
                 img-url (get response "img")]
             (with-open [o (clojure.java.io/output-stream "vcode.jpg")]
               (.write o (:body (client/get img-url {:as :byte-array}))))
             (sh/sh "shotwell" "vcode.jpg")
             (->>
              (client/get "http://pan.baidu.com/share/download" {:query-params
                                                                 (merge params
                                                                        {:vcode vcode
                                                                         :input (read-line)})})
              (:body)
              (json/read-str)
              (#(get % "dlink"))
              (client/head)
              (:trace-redirects)
              (first)
              ))))))

(get-bd-download-link
 "http://pan.baidu.com/s/1sJovW")
