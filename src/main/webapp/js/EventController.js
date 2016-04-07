angular.module('EventApp', ['federica.event', 'ui.bootstrap'])
  .controller('EventController', function ($scope, $log, EventService) {
  $scope.rooms = [];
  $scope.event = {};
  $scope.currentTab = 'rooms';
  $scope.shifts = ['Mañana', 'Tarde', 'Noche'];

  $scope.switchRoom = function(room) {
    if (_.includes($scope.event.rooms, room)) {
      _.remove($scope.event.rooms, function(r) {
        return r._id === room._id;
      });
    } else {
      room.bookingKind = 'Single';
      $scope.event.rooms.push(room);
    }
    var rooms = angular.element(document.querySelector( '#rooms' ));
    $log.log($scope.event.rooms);
    rooms.val(_.pluck($scope.event.rooms,'_id').join(','))
  }

  $scope.isRoomSelected = function(room) {
    if (_.some($scope.event.rooms, function(item) { return room._id === item._id; })) {
      return 'panel-primary';
    } else {
      return 'panel-default';
    }
  }

  $scope.gotoPrevTab = function(tab) {
    switch(tab) {
      case 'dates':
        $scope.currentTab = 'rooms';
        break;
      case 'equipment':
        $scope.currentTab = 'dates';
        break;
      case 'info':
        $scope.currentTab = 'equipment';
        break;
    }
  }

  $scope.gotoNextTab = function(tab) {
    $log.log(tab);
    switch(tab) {
      case 'rooms':
        $scope.currentTab = 'dates';
        break;
      case 'dates':
        $scope.currentTab = 'equipment';
        break;
      case 'equipment':
        $scope.currentTab = 'info';
        break;
    }
  }

  $scope.addDate = function(room) {
    if (!room.dates) {
      room.dates = [];
    }
    var date = { date: new Date(), shift: 'Mañana'};
    room.dates.push(date);
  }

  $scope.removeDate = function(room, date) {
    $log.log("removing");
    _.remove(room.dates, function(d) {
      return d === date;
    });
  }

  $scope.today = function() {
      $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function() {
      $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
      return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
    };

    $scope.toggleMin = function() {
      $scope.minDate = $scope.minDate ? null : new Date();
    };

    $scope.toggleMin();
    $scope.maxDate = new Date(2020, 5, 22);

    $scope.open1 = function() {
      $scope.popup1.opened = true;
    };

    $scope.open2 = function() {
      $scope.popup2.opened = true;
    };

    $scope.setDate = function(year, month, day) {
      $scope.dt = new Date(year, month, day);
    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];

    $scope.popup1 = {
      opened: false
    };

    $scope.popup2 = {
      opened: false
    };

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 1);
    $scope.events =
      [
        {
          date: tomorrow,
          status: 'full'
        },
        {
          date: afterTomorrow,
          status: 'partially'
        }
      ];

    $scope.getDayClass = function(date, mode) {
      if (mode === 'day') {
        var dayToCheck = new Date(date).setHours(0,0,0,0);

        for (var i = 0; i < $scope.events.length; i++) {
          var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

          if (dayToCheck === currentDay) {
            return $scope.events[i].status;
          }
        }
      }

      return '';
    };

  //init
  EventService.fetchEvent().then(function(response) {
    $scope.event = response;
  });
  EventService.listRooms().then(function(response) {
    $scope.rooms = response;
  });
});