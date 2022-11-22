(ns com.eelchat.feat.home
  (:require [com.biffweb :as biff]
            [com.eelchat.middleware :as mid]
            [com.eelchat.ui :as ui]
            [com.eelchat.util :as util]))

(def recaptcha-disclosure
  [:span "This site is protected by reCAPTCHA and the Google "
   [:a.text-teal-600.hover:underline
    {:href "https://policies.google.com/privacy"
     :target "_blank"}
    "Privacy Policy"] " and "
   [:a.text-teal-600.hover:underline
    {:href "https://policies.google.com/terms"
     :target "_blank"}
    "Terms of Service"] " apply."])

(defn signin-form [{:keys [recaptcha/site-key] :as sys}]
  (biff/form
   {:id "signin-form"
    :action "/auth/send"
    :class "sm:max-w-xs w-full"}
   [:input#email
    {:name "email"
     :type "email"
     :autocomplete "email"
     :placeholder "Enter your email address"
     :class '[border
              border-gray-300
              rounded
              w-full
              focus:border-teal-600
              focus:ring-teal-600]}]
   [:.h-3]
   [:sl-button
    (merge
     (when (util/email-signin-enabled? sys)
       {:data-sitekey site-key
        :data-callback "onSubscribe"
        :data-action "subscribe"})
     {:type "submit"
      :class '[g-recaptcha]})
    "Join the waitlist"]))

(def recaptcha-scripts
  [[:script {:src "https://www.google.com/recaptcha/api.js"
             :async "async"
             :defer "defer"}]
   [:script (biff/unsafe
             (str "function onSubscribe(token) { document.getElementById('signin-form').submit(); }"))]])

(defn home [sys]
  (ui/base
   {:base/head (when (util/email-signin-enabled? sys)
                 recaptcha-scripts)}
   [:nav
    [:h1 "Dahoma Creative"]
    [:button "Sign in"]] 
   [:.tester
    [:sl-card "hello"]
    [:.h-12.grow]
    [:img.w-40 {:src "/img/eel.svg"}]
    [:.h-6]
    [:.text-2xl.sm:text-3xl.font-semibold.sm:text-center.w-full
     "The world's finest discussion platform"]
    [:.h-2]
    [:.sm:text-lg.sm:text-center.w-full
     "Chat, forums, threads—eelchat has it all. Coming soon."]
    [:.h-6]
    (signin-form sys)
    [:.h-12 {:class "grow-[2]"}]
    [:.text-sm recaptcha-disclosure]
    [:.h-6]
    ]))

(def features
  {:routes ["" {:middleware [mid/wrap-redirect-signed-in]}
            ["/" {:get home}]]})
