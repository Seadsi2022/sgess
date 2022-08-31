import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEquestionnaire } from '../equestionnaire.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../equestionnaire.test-samples';

import { EquestionnaireService } from './equestionnaire.service';

const requireRestSample: IEquestionnaire = {
  ...sampleWithRequiredData,
};

describe('Equestionnaire Service', () => {
  let service: EquestionnaireService;
  let httpMock: HttpTestingController;
  let expectedResult: IEquestionnaire | IEquestionnaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EquestionnaireService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Equestionnaire', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const equestionnaire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(equestionnaire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Equestionnaire', () => {
      const equestionnaire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(equestionnaire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Equestionnaire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Equestionnaire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Equestionnaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEquestionnaireToCollectionIfMissing', () => {
      it('should add a Equestionnaire to an empty array', () => {
        const equestionnaire: IEquestionnaire = sampleWithRequiredData;
        expectedResult = service.addEquestionnaireToCollectionIfMissing([], equestionnaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equestionnaire);
      });

      it('should not add a Equestionnaire to an array that contains it', () => {
        const equestionnaire: IEquestionnaire = sampleWithRequiredData;
        const equestionnaireCollection: IEquestionnaire[] = [
          {
            ...equestionnaire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEquestionnaireToCollectionIfMissing(equestionnaireCollection, equestionnaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Equestionnaire to an array that doesn't contain it", () => {
        const equestionnaire: IEquestionnaire = sampleWithRequiredData;
        const equestionnaireCollection: IEquestionnaire[] = [sampleWithPartialData];
        expectedResult = service.addEquestionnaireToCollectionIfMissing(equestionnaireCollection, equestionnaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equestionnaire);
      });

      it('should add only unique Equestionnaire to an array', () => {
        const equestionnaireArray: IEquestionnaire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const equestionnaireCollection: IEquestionnaire[] = [sampleWithRequiredData];
        expectedResult = service.addEquestionnaireToCollectionIfMissing(equestionnaireCollection, ...equestionnaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const equestionnaire: IEquestionnaire = sampleWithRequiredData;
        const equestionnaire2: IEquestionnaire = sampleWithPartialData;
        expectedResult = service.addEquestionnaireToCollectionIfMissing([], equestionnaire, equestionnaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equestionnaire);
        expect(expectedResult).toContain(equestionnaire2);
      });

      it('should accept null and undefined values', () => {
        const equestionnaire: IEquestionnaire = sampleWithRequiredData;
        expectedResult = service.addEquestionnaireToCollectionIfMissing([], null, equestionnaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equestionnaire);
      });

      it('should return initial array if no Equestionnaire is added', () => {
        const equestionnaireCollection: IEquestionnaire[] = [sampleWithRequiredData];
        expectedResult = service.addEquestionnaireToCollectionIfMissing(equestionnaireCollection, undefined, null);
        expect(expectedResult).toEqual(equestionnaireCollection);
      });
    });

    describe('compareEquestionnaire', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEquestionnaire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEquestionnaire(entity1, entity2);
        const compareResult2 = service.compareEquestionnaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEquestionnaire(entity1, entity2);
        const compareResult2 = service.compareEquestionnaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEquestionnaire(entity1, entity2);
        const compareResult2 = service.compareEquestionnaire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
