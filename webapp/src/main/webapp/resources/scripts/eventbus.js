class EventBus {

    constructor() {
        this.subscribers = {};
    }


    post(eventType, someEvent) {

        let eventTypeSubscribers = this.subscribers[eventType];

        if (eventTypeSubscribers == undefined) {

            console.log("Subscribers not found for eventType : " + eventType);

        } else {

            for (var i = 0; i < eventTypeSubscribers.length; i++) {
                eventTypeSubscribers[i](someEvent);
            }

            console.log('Event posted for eventType : ' + eventType);
        }
    }

    subscribe (eventType, callback) {

        if (typeof (this.subscribers[eventType]) === 'undefined') {

            this.subscribers[eventType] = [];

        }

        if (typeof (callback) === 'function') {

            this.subscribers[eventType].push(callback);

            console.log('Subscriber added');

        } else {

            console.log("Callback must be function for event type : " + eventType);

        }
    }
}

let eventBus = new EventBus();
export default eventBus;