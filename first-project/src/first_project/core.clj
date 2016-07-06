(ns first-project.core
  (:gen-class))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn my-reduce
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn multiply-part
  [n part]
  (let [asym-str #"^left-"]
    (if (re-find asym-str (:name part))
      (repeat n
              {:name (clojure.string/replace (:name part) asym-str "")
               :size (:size part)})
      part)))

(defn expand-body-parts
  [expander initial-body-parts]
  (my-reduce expander [] initial-body-parts))

(defn symmetrize-body-parts
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts]
  (expand-body-parts (fn [final-body-parts part]
                      (into final-body-parts (set [part (matching-part part)])))
                     asym-body-parts))

(defn multiply-body-parts
  [n body-parts]
  (expand-body-parts (fn [final-body-parts part]
                      (into final-body-parts (multiply-part n part)))
                     body-parts))

(defn symmetrize-body-parts-old
  "Expects a sequence of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn hit
  [asym-body-parts]
  (let [sym-parts (symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn add-hundred [n] (+ n 100))

(defn dec-maker [n] #(- % n))

(defn map-set [f collection] (set (map f collection)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (multiply-body-parts 8 asym-hobbit-body-parts)))
