package practice.aws.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import practice.aws.domain.posts.QPosts;
import practice.aws.web.dto.PostsListResponseDto;
import practice.aws.web.dto.QPostsListResponseDto;

import javax.persistence.EntityManager;
import java.util.List;

import static practice.aws.domain.posts.QPosts.*;

@Repository
public class PostsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostsQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PostsListResponseDto> findAllDesc() {
        return queryFactory
                .select(new QPostsListResponseDto(
                        posts.id,
                        posts.title,
                        posts.author,
                        posts.modifiedDate))
                .from(posts)
                .orderBy(posts.id.desc())
                .fetch();
    }


}
