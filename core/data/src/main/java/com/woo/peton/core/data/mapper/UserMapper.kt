import com.woo.peton.core.data.remote.dto.UserDto
import com.woo.peton.domain.model.User

fun UserDto.toDomain(): User{
    return User(
        uid = uid,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        fcmToken = fcmToken,
        latitude = latitude,
        longitude = longitude,
        geoHash = geoHash,
        address = address,
        interests = interests
    )
}


fun User.toDto(): UserDto {
    return UserDto(
        uid = uid,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        fcmToken = fcmToken,
        latitude = latitude,
        longitude = longitude,
        geoHash = geoHash,
        address = address,
        interests = interests
    )
}
