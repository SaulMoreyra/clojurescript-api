(ns rest-api.routes.user-routes
  (:require ["express" :as express]
            [rest-api.services.user-service :as user-service]))

(def router (.Router express))

;; Obtener todos los usuarios
(.get router "/"
      (fn [req res]
        (-> (user-service/get-users)
            (.then (fn [users]
                     (.json res (clj->js users))))
            (.catch (fn [err]
                      (.status res 500)
                      (.json res (clj->js {:error "Error to get users"})))))))

;; Obtener el parámetro id de la URL y devolverlo retornal el servicio
(.get router "/:id"
      (fn [req res]
        (let [id (.-id (.-params req))]
          (-> (user-service/get-user id)
              (.then (fn [user]
                       (if user
                         (.json res (clj->js user))
                         ((.status res 404)
                          (.json res (clj->js {:error "User not found"}))))))
              (.catch (fn [err]
                        (.status res 500)
                        (.json res (clj->js {:error "Error getting user"}))))))))

;; Crear un nuevo usuario
(.post router "/"
       (fn [req res]
         (let [name (.-name (.-body req))
               email (.-email (.-body req))]
           (-> (user-service/create-user name email)
               (.then (fn [user]
                        (.json res (clj->js user))))
               (.catch (fn [err]
                         (.status res 500)
                         (.json res (clj->js {:error "Error creating user"}))))))))

(.put router "/:id"
      (fn [req res]
        (let [id (.-id (.-params req))
              name (.-name (.-body req))
              email (.-email (.-body req))]
          (-> (user-service/update-user id name email)
              (.then (fn [user]
                       (.json res (clj->js user))))
              (.catch (fn [err]
                        (.status res 500)
                        (.json res (clj->js {:error "Error updating user"}))))))))

;; ;; Eliminar un usuario por ID
(.delete router "/:id"
         (fn [req res]
           (let [id (.-id (.-params req))]
             (-> (user-service/delete-user id)
                 (.then (fn [user]
                          (.json res (clj->js user))))
                 (.catch (fn [err]
                           (.status res 500)
                           (.json res (clj->js {:error "Error deleting user"}))))))))

