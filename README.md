# FuturyzeDigital

Library used :

1 Retrofit.
2 Hilt Dependency injection
3 Fused Location API.
4 Databinding
5 LiveData
6 ViewBinding

Used MVVM architecture.

Brief work flow.

There are two modules in this project , "App" and "moduleForApiServices" . "App" is the default module and a separate
module is being created for storing the class related to Api Services.
Navigation component is being used to achieve single activity app eventhough only one fragment is used but to
demonstrate that if more than one fragment would be there then we could use the navigation component.

There are two apis are being used in this project , one is for fetching current temperature and one for fetching the
forecast of every 3 hours , although there was one api at "Open Weather API services" for getting the Forecast every hour but i chose for
every three hours forecast api because i thought we need to showcase the forecast at some interval so its fine even if we are showing at 3 hour interval although
the json response formate is same for both 3 hours and 1 hour forecast.

Room database is not used for storing the weather because initially it didn't came up into mind because it wasn't mention explictly
when i saw in "Libraries/APIs/Approaches to be used:" then suddenly it came into my mind , although in my live projects i store the data in room
and update the UI using Livedata because it's quite simple to use Livedata with Room as we don't need to explictly fetch the updated data from room because as
soon as the data changes in the Room database , Livedata observe the updated data and refresh the UI .
Over here it just not came into my mind that's why couldn't use.

Data is stored inside the Viewmodel because the api won't get called while rotation therefore UI could get the data from viewmodel .

Implemented the Hilt Dependency inject for injecting the Objects at various places wherever it is required.