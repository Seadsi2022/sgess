import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEunite } from '../eunite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../eunite.test-samples';

import { EuniteService } from './eunite.service';

const requireRestSample: IEunite = {
  ...sampleWithRequiredData,
};

describe('Eunite Service', () => {
  let service: EuniteService;
  let httpMock: HttpTestingController;
  let expectedResult: IEunite | IEunite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EuniteService);
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

    it('should create a Eunite', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eunite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eunite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Eunite', () => {
      const eunite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eunite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Eunite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Eunite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Eunite', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEuniteToCollectionIfMissing', () => {
      it('should add a Eunite to an empty array', () => {
        const eunite: IEunite = sampleWithRequiredData;
        expectedResult = service.addEuniteToCollectionIfMissing([], eunite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eunite);
      });

      it('should not add a Eunite to an array that contains it', () => {
        const eunite: IEunite = sampleWithRequiredData;
        const euniteCollection: IEunite[] = [
          {
            ...eunite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEuniteToCollectionIfMissing(euniteCollection, eunite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Eunite to an array that doesn't contain it", () => {
        const eunite: IEunite = sampleWithRequiredData;
        const euniteCollection: IEunite[] = [sampleWithPartialData];
        expectedResult = service.addEuniteToCollectionIfMissing(euniteCollection, eunite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eunite);
      });

      it('should add only unique Eunite to an array', () => {
        const euniteArray: IEunite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const euniteCollection: IEunite[] = [sampleWithRequiredData];
        expectedResult = service.addEuniteToCollectionIfMissing(euniteCollection, ...euniteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eunite: IEunite = sampleWithRequiredData;
        const eunite2: IEunite = sampleWithPartialData;
        expectedResult = service.addEuniteToCollectionIfMissing([], eunite, eunite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eunite);
        expect(expectedResult).toContain(eunite2);
      });

      it('should accept null and undefined values', () => {
        const eunite: IEunite = sampleWithRequiredData;
        expectedResult = service.addEuniteToCollectionIfMissing([], null, eunite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eunite);
      });

      it('should return initial array if no Eunite is added', () => {
        const euniteCollection: IEunite[] = [sampleWithRequiredData];
        expectedResult = service.addEuniteToCollectionIfMissing(euniteCollection, undefined, null);
        expect(expectedResult).toEqual(euniteCollection);
      });
    });

    describe('compareEunite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEunite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEunite(entity1, entity2);
        const compareResult2 = service.compareEunite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEunite(entity1, entity2);
        const compareResult2 = service.compareEunite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEunite(entity1, entity2);
        const compareResult2 = service.compareEunite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
