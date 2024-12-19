package rules;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.KieServices;
import models.Answer;
import models.PossibleAnswers;
import models.Question;

import java.util.Collection;

public class DecisionEngine {
    private KieSession kieSession;

    public DecisionEngine() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kieSession = kContainer.newKieSession("ksession-rules");
    }

    public Question getFirstQuestion() {
        Question question = new Question("Do you love money?");
        kieSession.insert(question);
        kieSession.fireAllRules();
        kieSession.delete(kieSession.getFactHandle(question));
        System.out.println("First question inserted: " + question.getText());
        Collection<?> facts = kieSession.getObjects();  // Zmieniony typ kolekcji na Collection<?>
        System.out.println("Objects in session: " + facts.size());

        // Debug: wypisanie obiekt�w w sesji
        for (Object fact : facts) {
            System.out.println("Object in session: " + fact);
        }

        return question;
    }

    public Question getNextQuestion(Answer answer) {
        
        kieSession.insert(answer);  // Wstaw odpowied� do sesji
        kieSession.fireAllRules();  // Uruchom regu�y
        kieSession.delete(kieSession.getFactHandle(answer));
        
        // Debug: sprawdzenie obiekt�w w sesji
        Collection<?> facts = kieSession.getObjects();  // Zmieniony typ kolekcji na Collection<?>
        System.out.println("Objects in session: " + facts.size());

        // Debug: wypisanie obiekt�w w sesji
        for (Object fact : facts) {
            System.out.println("Object in session: " + fact);
        }

        // Sprawdzamy w sesji pytania, kt�re s� nowe
        for (Object fact : facts) {
            if (fact instanceof Question) {
                Question q = (Question) fact;  // Rzutowanie na typ Question
                System.out.println(q.getText());
                kieSession.delete(kieSession.getFactHandle(q));
                return q;
            }
        }
        System.out.println("No more questions found.");
        return null;  // Brak kolejnych pyta�
    }
    
    public PossibleAnswers getPossibleAnswers(Answer answer) {
    	Collection<?> facts = kieSession.getObjects();
    	for (Object fact : facts) {
            if (fact instanceof PossibleAnswers) {
                PossibleAnswers answers = (PossibleAnswers) fact;
                    kieSession.delete(kieSession.getFactHandle(answers));
                    return answers;
            }
        }
    	return null;
    }
}
