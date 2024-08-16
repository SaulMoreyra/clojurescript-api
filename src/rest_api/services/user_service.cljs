(ns rest-api.services.user-service
  (:require [rest-api.db.client :refer [get-prisma]]))

;; Obtener la instancia de prisma
(def prisma (get-prisma))

(defn get-users []
  (-> (.-user prisma)
      (.findMany)
      (.then (fn [users] users))
      (.catch (fn [err] ({:error (.-message err)})))))

(defn get-user [id]
  (-> (.-user prisma)
      (.findUnique (clj->js {:where {:id id}}))
      (.then (fn [user] user))
      (.catch (fn [err] ({:error (.-message err)})))))

(defn create-user [name email]
  (-> (.-user prisma)
      (.findUnique (clj->js {:where {:email email}}))
      (.then (fn [existing-user]
               (if existing-user
                 {:error "Email already exists"}
                 (-> (.-user prisma)
                     (.create (clj->js {:data {:name name :email email}}))
                     (.then (fn [created-user] created-user))
                     (.catch (fn [err] ({:error (.-message err)})))))))))

(defn update-user [id name email]
  (-> (.-user prisma)
      (.findUnique (clj->js {:where {:id id}}))
      (.then (fn [existing-user]
               (if (not existing-user)
                 {:error "User not found"}
                 (-> (.-user prisma)
                     (.update (clj->js {:where {:id id}
                                        :data {:name name :email email}}))
                     (.then (fn [updated-user] updated-user))
                     (.catch (fn [err] ({:error (.-message err)})))))))))

(defn delete-user [id]
  (-> (.-user prisma)
      (.delete (clj->js {:where {:id id}}))
      (.then (fn [deleted-user] deleted-user))
      (.catch (fn [err] ({:error (.-message err)})))))